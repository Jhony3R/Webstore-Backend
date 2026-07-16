package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.dto.Venta.DetalleVentaRequestDTO;
import jhony.ruiz.sigevi.dto.VentaRequestDTO;
import jhony.ruiz.sigevi.exception.*;
import jhony.ruiz.sigevi.model.*;
import jhony.ruiz.sigevi.repository.*;
import jhony.ruiz.sigevi.service.IVentaService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl extends CRUDImpl<Venta, Integer> implements IVentaService {
    private final IVentaRepository ventaRepository;
    private final IProductoRepository productoRepository;
    private final ICajaRepository cajaRepository;
    private final IClienteRepository clienteRepository;
    private final IUsuarioRepository usuarioRepository;

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    protected IGenericRepo<Venta, Integer> getRepo() {
        return ventaRepository;
    }

    @Override
    @Transactional
    public Venta registrarVenta(String username, VentaRequestDTO requestDTO) {

        Caja caja = cajaRepository
                .findByEstadoCajaAndUsuarioUsername(EstadoCaja.ABIERTA, username)
                .orElseThrow(() -> new CajaNoAbiertaException(
                        "No tienes una caja abierta. Debes aperturar caja antes de registrar ventas."));

        Cliente cliente = null;
        if (requestDTO.getIdCliente() != null) {
            cliente = clienteRepository.findById(requestDTO.getIdCliente())
                    .orElseThrow(() -> new ModelNotFoundException("Cliente no encontrado: " + requestDTO.getIdCliente()));
        }

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado: " + username));

        List<DetalleVenta> detalles = new ArrayList<>();
        double subTotal = 0.0;

        for (DetalleVentaRequestDTO item : requestDTO.getDetalles()) {
            Producto producto = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new ModelNotFoundException("Producto no encontrado: " + item.getIdProducto()));

            if (!producto.isActivo()) {
                throw new StockInsuficienteException(
                        "El producto '" + producto.getDescripcion() + "' no está disponible.");
            }

            if (producto.getStockActual() <= 0) {
                throw new StockInsuficienteException(
                        "Stock agotado para: " + producto.getDescripcion());
            }

            if (producto.getStockActual() < item.getCantidad()) {
                throw new StockInsuficienteException(
                        "Stock insuficiente para '" + producto.getDescripcion() + "'. Disponible: "
                                + producto.getStockActual() + ", solicitado: " + item.getCantidad());
            }

            producto.setStockActual(producto.getStockActual() - item.getCantidad());
            productoRepository.save(producto);

            double subTotalItem = producto.getPrecioVenta() * item.getCantidad();
            subTotal += subTotalItem;

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecioVenta());
            detalle.setSubTotal(subTotalItem);
            detalles.add(detalle);
        }

        double valorDescuento = calcularDescuento(requestDTO, subTotal);
        double total = subTotal - valorDescuento;

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setSubTotal(subTotal);
        venta.setValorDescuento(valorDescuento);
        venta.setTotal(total);
        venta.setTipodescuento(requestDTO.getTipoDescuento());
        venta.setMetodoPago(requestDTO.getMetodoPago());
        venta.setEstadoVenta(EstadoVenta.COMPLETADA);
        venta.setCliente(cliente);
        venta.setUsuario(usuario);
        venta.setCaja(caja);
        venta.setDetalles(detalles);
        detalles.forEach(d -> d.setVenta(venta));

        Venta ventaGuardada = ventaRepository.save(venta);

        ventaGuardada.setNumeroComprobante(String.format("V%06d", ventaGuardada.getIdVenta()));
        ventaGuardada = ventaRepository.save(ventaGuardada);

        actualizarAcumuladosCaja(caja, requestDTO.getMetodoPago(), total);
        cajaRepository.save(caja);

        return ventaGuardada;
    }

    @Override
    @Transactional
    public Venta anularVenta(Integer idVenta, String motivo) {

        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new ModelNotFoundException("Venta no encontrada: " + idVenta));

        if (venta.getEstadoVenta() == EstadoVenta.ANULADA) {
            throw new Ventayaanuladaexception(
                    "La venta " + venta.getNumeroComprobante() + " ya se encuentra anulada.");
        }

        Caja caja = venta.getCaja();

        // RF-05 / CU04 (curso alterno): si la caja del turno ya fue cerrada,
        // no se permite anular directamente; debe manejarse como devolución.
        if (caja != null && caja.getEstadoCaja() == EstadoCaja.CERRADA) {
            throw new Cajacerradaexception(
                    "No se puede anular esta venta porque la caja del turno en que se registró ya está cerrada. "
                            + "Este caso debe registrarse como una devolución.");
        }

        // Revertir el stock de cada producto vendido
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStockActual(producto.getStockActual() + detalle.getCantidad());
            productoRepository.save(producto);
        }

        // Revertir el acumulado de la caja (lo contrario a actualizarAcumuladosCaja)
        if (caja != null) {
            revertirAcumuladosCaja(caja, venta.getMetodoPago(), venta.getTotal());
            cajaRepository.save(caja);
        }

        venta.setEstadoVenta(EstadoVenta.ANULADA);
        venta.setMotivoAnulacion(motivo);
        venta.setFechaAnulacion(LocalDateTime.now());

        return ventaRepository.save(venta);
    }

    private double calcularDescuento(VentaRequestDTO requestDTO, double subTotal) {
        if (requestDTO.getTipoDescuento() == null || requestDTO.getValorDescuentoInput() == null
                || requestDTO.getValorDescuentoInput() <= 0) {
            return 0.0;
        }

        if (requestDTO.getTipoDescuento() == TipoDescuento.PORCENTAJE) {
            if (requestDTO.getValorDescuentoInput() > 100) {
                throw new IllegalArgumentException("El descuento por porcentaje no puede ser mayor a 100%");
            }
            return subTotal * (requestDTO.getValorDescuentoInput() / 100.0);
        }

        double descuento = requestDTO.getValorDescuentoInput();
        if (descuento > subTotal) {
            throw new IllegalArgumentException("El descuento no puede ser mayor al subtotal de la venta");
        }
        return descuento;
    }

    private void actualizarAcumuladosCaja(Caja caja, MetodoPago metodoPago, double total) {
        switch (metodoPago) {
            case EFECTIVO -> caja.setSaldoEsperadoEfectivo(
                    (caja.getSaldoEsperadoEfectivo() == null ? 0.0 : caja.getSaldoEsperadoEfectivo()) + total);
            case YAPE -> caja.setTotalYape((caja.getTotalYape() == null ? 0.0 : caja.getTotalYape()) + total);
            case PLIN -> caja.setTotalPlin((caja.getTotalPlin() == null ? 0.0 : caja.getTotalPlin()) + total);
            case TARJETA -> caja.setTotalTarjeta((caja.getTotalTarjeta() == null ? 0.0 : caja.getTotalTarjeta()) + total);
            case TRANSFERENCIA -> caja.setTotalTransferencia(
                    (caja.getTotalTransferencia() == null ? 0.0 : caja.getTotalTransferencia()) + total);
        }
    }

    private void revertirAcumuladosCaja(Caja caja, MetodoPago metodoPago, double total) {
        switch (metodoPago) {
            case EFECTIVO -> caja.setSaldoEsperadoEfectivo(
                    (caja.getSaldoEsperadoEfectivo() == null ? 0.0 : caja.getSaldoEsperadoEfectivo()) - total);
            case YAPE -> caja.setTotalYape((caja.getTotalYape() == null ? 0.0 : caja.getTotalYape()) - total);
            case PLIN -> caja.setTotalPlin((caja.getTotalPlin() == null ? 0.0 : caja.getTotalPlin()) - total);
            case TARJETA -> caja.setTotalTarjeta((caja.getTotalTarjeta() == null ? 0.0 : caja.getTotalTarjeta()) - total);
            case TRANSFERENCIA -> caja.setTotalTransferencia(
                    (caja.getTotalTransferencia() == null ? 0.0 : caja.getTotalTransferencia()) - total);
        }
    }

    public byte[] generarComprobantePdf(Integer idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new ModelNotFoundException("Venta no encontrada: " + idVenta));

        try {
            InputStream jrxmlStream = new ClassPathResource("reports/comprobante_venta.jrxml").getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

            Map<String, Object> params = new HashMap<>();
            params.put("numeroComprobante", venta.getNumeroComprobante());
            params.put("fecha", venta.getFecha().format(FORMATO_FECHA));
            params.put("clienteNombre", nombreClienteParaComprobante(venta));
            params.put("metodoPago", venta.getMetodoPago().name());
            params.put("subTotal", String.format("%.2f", venta.getSubTotal()));
            params.put("valorDescuento", String.format("%.2f", venta.getValorDescuento()));
            params.put("total", String.format("%.2f", venta.getTotal()));

            List<Map<String, Object>> detalleData = venta.getDetalles().stream()
                    .map(this::mapDetalle)
                    .collect(Collectors.toList());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(detalleData);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            ajustarAltoAlContenido(jasperPrint);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el comprobante PDF: " + e.getMessage(), e);
        }
    }

    private String nombreClienteParaComprobante(Venta venta) {
        if (venta.getCliente() == null) {
            return "General";
        }
        var c = venta.getCliente();
        return String.join(" ", c.getNombres(), c.getApellidoPaterno(), c.getApellidoMaterno());
    }

    private Map<String, Object> mapDetalle(DetalleVenta d) {
        Map<String, Object> row = new HashMap<>();
        row.put("descripcion", d.getProducto().getDescripcion());
        row.put("cantidad", d.getCantidad());
        row.put("precioUnitario", d.getPrecioUnitario());
        row.put("subtotal", d.getSubTotal());
        return row;
    }

    private void ajustarAltoAlContenido(JasperPrint jasperPrint) {
        if (jasperPrint.getPages().isEmpty()) {
            return;
        }

        int altoContenido = jasperPrint.getPages().stream()
                .flatMap(page -> page.getElements().stream())
                .mapToInt(el -> el.getY() + el.getHeight())
                .max()
                .orElse(jasperPrint.getPageHeight());

        int margenInferior = 15;
        jasperPrint.setPageHeight(altoContenido + margenInferior);
    }
}

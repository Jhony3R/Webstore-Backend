package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.dto.Venta.DetalleVentaRequestDTO;
import jhony.ruiz.sigevi.dto.VentaRequestDTO;
import jhony.ruiz.sigevi.exception.CajaNoAbiertaException;
import jhony.ruiz.sigevi.exception.ModelNotFoundException;
import jhony.ruiz.sigevi.exception.StockInsuficienteException;
import jhony.ruiz.sigevi.model.*;
import jhony.ruiz.sigevi.repository.*;
import jhony.ruiz.sigevi.service.IVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl extends CRUDImpl<Venta, Integer> implements IVentaService {
    private final IVentaRepository ventaRepository;
    private final IProductoRepository productoRepository;
    private final ICajaRepository cajaRepository;
    private final IClienteRepository clienteRepository;
    private final IUsuarioRepository usuarioRepository;

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

        Cliente cliente = clienteRepository.findById(requestDTO.getIdCliente())
                .orElseThrow(() -> new ModelNotFoundException("Cliente no encontrado: " + requestDTO.getIdCliente()));

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
}

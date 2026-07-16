package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.dto.Compra.CompraRequestDTO;
import jhony.ruiz.sigevi.dto.Compra.DetalleCompraRequestDTO;
import jhony.ruiz.sigevi.dto.CompraDTO;
import jhony.ruiz.sigevi.exception.Recursonoencontradoexception;
import jhony.ruiz.sigevi.model.*;
import jhony.ruiz.sigevi.repository.*;
import jhony.ruiz.sigevi.service.ICompraService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraServiceImpl extends CRUDImpl<Compra, Integer> implements ICompraService {
    private final ICompraRepository compraRepository;
    private final IDetalleCompraRepository detalleCompraRepository;
    private final IProductoRepository productoRepository;
    private final IProveedorRepository proveedorRepository;
    private final IUsuarioRepository usuarioRepository;
    private final MapperUtil mapperUtil;

    @Override
    protected IGenericRepo<Compra, Integer> getRepo() {
        return compraRepository;
    }

    @Override
    @Transactional
    public CompraDTO registrarCompra(CompraRequestDTO dto) {

        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new Recursonoencontradoexception(
                        "No se encontró el proveedor con id " + dto.getIdProveedor()));

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new Recursonoencontradoexception(
                        "No se encontró el usuario con id " + dto.getIdUsuario()));

        Compra compra = new Compra();
        compra.setNumeroComprobante(dto.getNumeroComprobante());
        compra.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDateTime.now());
        compra.setProveedor(proveedor);
        compra.setUsuario(usuario);
        compra.setMontoTotal(0.0);

        Compra compraGuardada = compraRepository.save(compra);

        double montoTotal = 0.0;
        List<DetalleCompra> detallesGuardados = new ArrayList<>();

        for (DetalleCompraRequestDTO detalleReq : dto.getDetalles()) {

            Producto producto = productoRepository.findById(detalleReq.getIdProducto())
                    .orElseThrow(() -> new Recursonoencontradoexception(
                            "No se encontró el producto con id " + detalleReq.getIdProducto()));

            double subTotal = detalleReq.getCantidad() * detalleReq.getPrecioUnitario();

            DetalleCompra detalle = new DetalleCompra();
            detalle.setCompra(compraGuardada);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleReq.getCantidad());
            detalle.setPrecioUnitario(detalleReq.getPrecioUnitario());
            detalle.setSubTotal(subTotal);
            detallesGuardados.add(detalleCompraRepository.save(detalle));

            producto.setStockActual(producto.getStockActual() + detalleReq.getCantidad());
            productoRepository.save(producto);

            montoTotal += subTotal;
        }

        compraGuardada.setMontoTotal(montoTotal);
        compraGuardada = compraRepository.save(compraGuardada);

        return mapperUtil.map(compraGuardada, CompraDTO.class);
    }
}

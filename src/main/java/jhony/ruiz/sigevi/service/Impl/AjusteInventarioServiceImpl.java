package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.dto.AjusteInventario.AjusteInventarioRequestDTO;
import jhony.ruiz.sigevi.exception.ModelNotFoundException;
import jhony.ruiz.sigevi.exception.StockInsuficienteException;
import jhony.ruiz.sigevi.model.AjusteInventario;
import jhony.ruiz.sigevi.model.Producto;
import jhony.ruiz.sigevi.model.TipoAjusteInventario;
import jhony.ruiz.sigevi.model.Usuario;
import jhony.ruiz.sigevi.repository.IAjusteInventarioRepository;
import jhony.ruiz.sigevi.repository.IGenericRepo;
import jhony.ruiz.sigevi.repository.IProductoRepository;
import jhony.ruiz.sigevi.repository.IUsuarioRepository;
import jhony.ruiz.sigevi.service.IAjusteInventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AjusteInventarioServiceImpl extends CRUDImpl<AjusteInventario, Integer> implements IAjusteInventarioService {
    private final IAjusteInventarioRepository ajusteInventarioRepository;
    private final IProductoRepository productoRepository;
    private final IUsuarioRepository usuarioRepository;

    @Override
    protected IGenericRepo<AjusteInventario, Integer> getRepo() {
        return ajusteInventarioRepository;
    }

    @Override
    @Transactional
    public AjusteInventario registrarAjuste(AjusteInventarioRequestDTO request) {
        Producto producto = productoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new ModelNotFoundException(
                        "No existe un producto con id: " + request.getIdProducto()));

        Usuario usuarioActual = obtenerUsuarioAutenticado();

        int stockAnterior = producto.getStockActual();
        int stockNuevo = calcularStockNuevo(stockAnterior, request.getCantidad(), request.getTipoAjusteInventario());

        if (stockNuevo < 0) {
            throw new StockInsuficienteException(
                    "El ajuste dejaría el stock en negativo (" + stockNuevo + "). Stock actual: " + stockAnterior);
        }

        producto.setStockActual(stockNuevo);
        productoRepository.save(producto);

        AjusteInventario ajuste = new AjusteInventario();
        ajuste.setCantidad(request.getCantidad());
        ajuste.setMotivo(request.getMotivo());
        ajuste.setTipoAjusteInventario(request.getTipoAjusteInventario());
        ajuste.setStockAnterior(stockAnterior);
        ajuste.setStockNuevo(stockNuevo);
        ajuste.setFecha(LocalDateTime.now());
        ajuste.setProducto(producto);
        ajuste.setUsuario(usuarioActual);

        return ajusteInventarioRepository.save(ajuste);
    }

    private int calcularStockNuevo(int stockAnterior, int cantidad, TipoAjusteInventario tipo) {
        return switch (tipo) {
            case MERMA, PERDIDA -> stockAnterior - cantidad;
            case DEVOLUCION, INGRESO_COMPRA -> stockAnterior + cantidad;
            case CORRECCION -> cantidad;
        };
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ModelNotFoundException("Usuario autenticado no encontrado: " + username));
    }

    @Override
    public AjusteInventario update(Integer id, AjusteInventario t) {
        throw new UnsupportedOperationException(
                "Los ajustes de inventario no se editan. Elimina el ajuste y registra uno nuevo si te equivocaste.");
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        AjusteInventario ajuste = ajusteInventarioRepository.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("El ID no existe: " + id));

        Producto producto = ajuste.getProducto();
        int delta = ajuste.getStockNuevo() - ajuste.getStockAnterior();
        int stockTrasReversion = producto.getStockActual() - delta;

        if (stockTrasReversion < 0) {
            throw new StockInsuficienteException(
                    "No se puede eliminar este ajuste: el stock resultante quedaría en " + stockTrasReversion +
                            ". Es probable que parte de ese stock ya se haya vendido o movido.");
        }

        producto.setStockActual(stockTrasReversion);
        productoRepository.save(producto);

        ajusteInventarioRepository.deleteById(id);
    }
}

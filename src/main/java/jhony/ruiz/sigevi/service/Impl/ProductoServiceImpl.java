package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.model.Producto;
import jhony.ruiz.sigevi.repository.IProductoRepository;
import jhony.ruiz.sigevi.repository.IGenericRepo;
import jhony.ruiz.sigevi.service.IProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl extends CRUDImpl<Producto, Integer> implements IProductoService {
    private final IProductoRepository productoRepository;

    @Override
    protected IGenericRepo<Producto, Integer> getRepo() {
        return productoRepository;
    }
}

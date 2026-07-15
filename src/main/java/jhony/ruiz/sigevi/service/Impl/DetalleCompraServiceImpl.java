package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.model.DetalleCompra;
import jhony.ruiz.sigevi.repository.IDetalleCompraRepository;
import jhony.ruiz.sigevi.repository.IGenericRepo;
import jhony.ruiz.sigevi.service.IDetalleCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetalleCompraServiceImpl extends CRUDImpl<DetalleCompra, Integer> implements IDetalleCompraService {
    private final IDetalleCompraRepository detalleCompraRepository;

    @Override
    protected IGenericRepo<DetalleCompra, Integer> getRepo() {
        return detalleCompraRepository;
    }
}

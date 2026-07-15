package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.model.Compra;
import jhony.ruiz.sigevi.repository.ICompraRepository;
import jhony.ruiz.sigevi.repository.IGenericRepo;
import jhony.ruiz.sigevi.service.ICompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompraServiceImpl extends CRUDImpl<Compra, Integer> implements ICompraService {
    private final ICompraRepository compraRepository;

    @Override
    protected IGenericRepo<Compra, Integer> getRepo() {
        return compraRepository;
    }
}

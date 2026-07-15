package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.model.Venta;
import jhony.ruiz.sigevi.repository.IVentaRepository;
import jhony.ruiz.sigevi.repository.IGenericRepo;
import jhony.ruiz.sigevi.service.IVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl extends CRUDImpl<Venta, Integer> implements IVentaService {
    private final IVentaRepository ventaRepository;

    @Override
    protected IGenericRepo<Venta, Integer> getRepo() {
        return ventaRepository;
    }
}

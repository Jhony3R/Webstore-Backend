package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.model.DetalleVenta;
import jhony.ruiz.sigevi.repository.IDetalleVentaRepository;
import jhony.ruiz.sigevi.repository.IGenericRepo;
import jhony.ruiz.sigevi.service.IDetalleVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetalleVentaServiceImpl extends CRUDImpl<DetalleVenta, Integer> implements IDetalleVentaService {
    private final IDetalleVentaRepository detalleVentaRepository;

    @Override
    protected IGenericRepo<DetalleVenta, Integer> getRepo() {
        return detalleVentaRepository;
    }
}

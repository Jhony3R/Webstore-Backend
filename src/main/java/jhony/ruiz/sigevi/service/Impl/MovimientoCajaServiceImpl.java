package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.model.MovimientoCaja;
import jhony.ruiz.sigevi.repository.IMovimientoCajaRepository;
import jhony.ruiz.sigevi.repository.IGenericRepo;
import jhony.ruiz.sigevi.service.IMovimientoCajaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovimientoCajaServiceImpl extends CRUDImpl<MovimientoCaja, Integer> implements IMovimientoCajaService {
    private final IMovimientoCajaRepository movimientoCajaRepository;

    @Override
    protected IGenericRepo<MovimientoCaja, Integer> getRepo() {
        return movimientoCajaRepository;
    }
}

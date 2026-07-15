package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.model.Caja;
import jhony.ruiz.sigevi.repository.ICajaRepository;
import jhony.ruiz.sigevi.repository.IGenericRepo;
import jhony.ruiz.sigevi.service.ICajaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CajaServiceImpl extends CRUDImpl<Caja, Integer> implements ICajaService {
    private final ICajaRepository cajaRepository;

    @Override
    protected IGenericRepo<Caja, Integer> getRepo() {
        return cajaRepository;
    }
}

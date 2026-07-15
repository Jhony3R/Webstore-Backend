package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.model.AjusteInventario;
import jhony.ruiz.sigevi.repository.IAjusteInventarioRepository;
import jhony.ruiz.sigevi.repository.IGenericRepo;
import jhony.ruiz.sigevi.service.IAjusteInventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AjusteInventarioServiceImpl extends CRUDImpl<AjusteInventario, Integer> implements IAjusteInventarioService {
    private final IAjusteInventarioRepository ajusteInventarioRepository;

    @Override
    protected IGenericRepo<AjusteInventario, Integer> getRepo() {
        return ajusteInventarioRepository;
    }
}

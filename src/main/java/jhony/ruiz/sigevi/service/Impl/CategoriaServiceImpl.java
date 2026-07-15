package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.model.Categoria;
import jhony.ruiz.sigevi.repository.ICategoriaRepository;
import jhony.ruiz.sigevi.repository.IGenericRepo;
import jhony.ruiz.sigevi.service.ICategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl extends CRUDImpl<Categoria, Integer> implements ICategoriaService {
    private final ICategoriaRepository categoriaRepository;

    @Override
    protected IGenericRepo<Categoria, Integer> getRepo() {
        return categoriaRepository;
    }
}

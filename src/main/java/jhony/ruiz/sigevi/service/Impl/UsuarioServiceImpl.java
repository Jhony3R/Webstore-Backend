package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.model.Usuario;
import jhony.ruiz.sigevi.repository.IUsuarioRepository;
import jhony.ruiz.sigevi.repository.IGenericRepo;
import jhony.ruiz.sigevi.service.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl extends CRUDImpl<Usuario, Integer> implements IUsuarioService {
    private final IUsuarioRepository usuarioRepository;

    @Override
    protected IGenericRepo<Usuario, Integer> getRepo() {
        return usuarioRepository;
    }
}

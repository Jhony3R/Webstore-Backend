package jhony.ruiz.sigevi.repository;

import jhony.ruiz.sigevi.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioRepository extends IGenericRepo<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);

    List<Usuario> findByActivo(Boolean activo);

    Optional<Usuario> findByUsernameAndActivo(String username, Boolean activo);
}

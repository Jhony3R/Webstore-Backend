package jhony.ruiz.sigevi.repository;

import jhony.ruiz.sigevi.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuarioRepository extends IGenericRepo<Usuario, Integer> {
    Usuario findOneByUsername(String username);

    Optional<Usuario> findByUsername(String username);
}

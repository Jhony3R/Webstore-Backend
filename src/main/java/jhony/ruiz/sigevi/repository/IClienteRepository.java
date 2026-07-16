package jhony.ruiz.sigevi.repository;

import jhony.ruiz.sigevi.model.Cliente;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IClienteRepository extends IGenericRepo<Cliente, Integer> {
    Optional<Cliente> findByNumeroDocumento(String numeroDocumento);
}

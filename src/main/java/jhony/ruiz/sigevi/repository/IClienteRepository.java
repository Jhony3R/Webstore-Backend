package jhony.ruiz.sigevi.repository;

import jhony.ruiz.sigevi.model.Cliente;
import org.springframework.stereotype.Repository;

@Repository
public interface IClienteRepository extends IGenericRepo<Cliente, Integer> {
}

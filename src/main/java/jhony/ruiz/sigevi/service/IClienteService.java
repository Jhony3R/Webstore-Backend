package jhony.ruiz.sigevi.service;

import jhony.ruiz.sigevi.model.Cliente;

import java.util.Optional;

public interface IClienteService extends ICRUD<Cliente, Integer> {
    Optional<Cliente> findByNumeroDocumento(String numeroDocumento);
}

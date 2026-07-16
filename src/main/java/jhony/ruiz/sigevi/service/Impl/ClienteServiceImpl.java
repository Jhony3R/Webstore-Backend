package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.model.Cliente;
import jhony.ruiz.sigevi.repository.IClienteRepository;
import jhony.ruiz.sigevi.repository.IGenericRepo;
import jhony.ruiz.sigevi.service.IClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl extends CRUDImpl<Cliente, Integer> implements IClienteService {
    private final IClienteRepository clienteRepository;

    @Override
    protected IGenericRepo<Cliente, Integer> getRepo() {
        return clienteRepository;
    }

    @Override
    public Optional<Cliente> findByNumeroDocumento(String numeroDocumento) {
        return clienteRepository.findByNumeroDocumento(numeroDocumento);
    }
}

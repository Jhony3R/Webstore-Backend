package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.model.Proveedor;
import jhony.ruiz.sigevi.repository.IProveedorRepository;
import jhony.ruiz.sigevi.repository.IGenericRepo;
import jhony.ruiz.sigevi.service.IProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl extends CRUDImpl<Proveedor, Integer> implements IProveedorService {
    private final IProveedorRepository proveedorRepository;

    @Override
    protected IGenericRepo<Proveedor, Integer> getRepo() {
        return proveedorRepository;
    }
}

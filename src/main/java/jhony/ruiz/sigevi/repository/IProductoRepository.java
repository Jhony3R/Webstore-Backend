package jhony.ruiz.sigevi.repository;

import jhony.ruiz.sigevi.model.Producto;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductoRepository extends IGenericRepo<Producto, Integer> {
}

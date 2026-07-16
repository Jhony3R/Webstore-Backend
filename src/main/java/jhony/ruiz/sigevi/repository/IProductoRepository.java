package jhony.ruiz.sigevi.repository;

import jhony.ruiz.sigevi.dto.Reporte.EstadoInventarioDTO;
import jhony.ruiz.sigevi.model.Producto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductoRepository extends IGenericRepo<Producto, Integer> {
    @Query("""
    SELECT new jhony.ruiz.sigevi.dto.Reporte.EstadoInventarioDTO(
        p.codigo, p.descripcion, c.nombre, p.stockActual, p.stockMinimo, p.precioVenta
    )
    FROM Producto p
    LEFT JOIN p.categoria c
    WHERE p.activo = true
    ORDER BY p.stockActual ASC
""")
    List<EstadoInventarioDTO> reporteEstadoInventario();
}

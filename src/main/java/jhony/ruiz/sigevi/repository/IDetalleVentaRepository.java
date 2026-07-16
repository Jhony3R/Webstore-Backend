package jhony.ruiz.sigevi.repository;

import jhony.ruiz.sigevi.dto.Reporte.ProductoMasVendidoDTO;
import jhony.ruiz.sigevi.model.DetalleVenta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IDetalleVentaRepository extends IGenericRepo<DetalleVenta, Integer> {
    @Query("""
    SELECT new jhony.ruiz.sigevi.dto.Reporte.ProductoMasVendidoDTO(
        p.codigo, p.descripcion, c.nombre, SUM(d.cantidad), SUM(d.subTotal)
    )
    FROM DetalleVenta d
    JOIN d.venta v
    JOIN d.producto p
    LEFT JOIN p.categoria c
    WHERE v.estadoVenta <> jhony.ruiz.sigevi.model.EstadoVenta.ANULADA
      AND date(v.fecha) BETWEEN :fechaInicio AND :fechaFin
    GROUP BY p.idProducto, p.codigo, p.descripcion, c.nombre
    ORDER BY SUM(d.cantidad) DESC
""")
    List<ProductoMasVendidoDTO> reporteProductosMasVendidos(@Param("fechaInicio") LocalDate fechaInicio,
                                                            @Param("fechaFin") LocalDate fechaFin);
}

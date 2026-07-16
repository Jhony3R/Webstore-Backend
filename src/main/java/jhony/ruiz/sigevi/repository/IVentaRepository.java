package jhony.ruiz.sigevi.repository;

import jhony.ruiz.sigevi.dto.Reporte.VentaDiariaDTO;
import jhony.ruiz.sigevi.model.Venta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IVentaRepository extends IGenericRepo<Venta, Integer> {
    @Query("""
    SELECT date(v.fecha), v.metodoPago, COUNT(v.idVenta), SUM(v.total)
    FROM Venta v
    WHERE v.estadoVenta <> jhony.ruiz.sigevi.model.EstadoVenta.ANULADA
      AND date(v.fecha) BETWEEN :fechaInicio AND :fechaFin
    GROUP BY date(v.fecha), v.metodoPago
    ORDER BY date(v.fecha)
""")
    List<Object[]> ventasAgrupadasPorFechaYMetodo(@Param("fechaInicio") LocalDate fechaInicio,
                                                  @Param("fechaFin") LocalDate fechaFin);

    @Query("""
    SELECT date(v.fecha), SUM(v.total)
    FROM Venta v
    WHERE v.estadoVenta <> jhony.ruiz.sigevi.model.EstadoVenta.ANULADA
      AND date(v.fecha) BETWEEN :fechaInicio AND :fechaFin
    GROUP BY date(v.fecha)
""")
    List<Object[]> sumVentasPorFecha(@Param("fechaInicio") LocalDate fechaInicio,
                                     @Param("fechaFin") LocalDate fechaFin);
}

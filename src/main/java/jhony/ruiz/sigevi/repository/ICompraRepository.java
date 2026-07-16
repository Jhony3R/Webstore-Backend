package jhony.ruiz.sigevi.repository;

import jhony.ruiz.sigevi.model.Compra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ICompraRepository extends IGenericRepo<Compra, Integer> {
    @Query("""
    SELECT date(c.fecha), SUM(c.montoTotal)
    FROM Compra c
    WHERE date(c.fecha) BETWEEN :fechaInicio AND :fechaFin
    GROUP BY date(c.fecha)
""")
    List<Object[]> sumComprasPorFecha(@Param("fechaInicio") LocalDate fechaInicio,
                                      @Param("fechaFin") LocalDate fechaFin);
}

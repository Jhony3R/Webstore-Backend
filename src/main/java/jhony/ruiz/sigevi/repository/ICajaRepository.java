package jhony.ruiz.sigevi.repository;

import jhony.ruiz.sigevi.model.Caja;
import jhony.ruiz.sigevi.model.EstadoCaja;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICajaRepository extends IGenericRepo<Caja, Integer> {
    @Query(" SELECT c FROM Caja c WHERE c.estadoCaja = :estado ")
    Optional<Caja> findByEstadoCaja(EstadoCaja estado);

    @Query(" SELECT c FROM Caja c WHERE c.estadoCaja = :estado AND c.usuario.idUsuario = :idUsuario ")
    Optional<Caja> findByEstadoCajaAndUsuario(@Param("estado") EstadoCaja estado, @Param("idUsuario") Integer idUsuario);

    @Query(" SELECT c FROM Caja c WHERE c.estadoCaja = :estado AND c.usuario.username = :username ")
    Optional<Caja> findByEstadoCajaAndUsuarioUsername(@Param("estado") EstadoCaja estado, @Param("username") String username);
}

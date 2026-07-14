package jhony.ruiz.sigevi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tbl_movimientocaja")
public class MovimientoCaja {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @EqualsAndHashCode.Include
    private Integer idMovimientoCaja;

    private String concepto;

    private String monto;

    private boolean fecha;

    @Enumerated(EnumType.STRING)
    private TipoMovimientoCaja tipoMovimientoCaja;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;

    @ManyToOne
    @JoinColumn(name = "id_caja", nullable = false)
    private Caja caja;

    @ManyToOne
    @JoinColumn(name = "id_venta", nullable = false)
    private Venta venta;

}


package jhony.ruiz.sigevi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tbl_ajusteinventario")
public class AjusteInventario {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @EqualsAndHashCode.Include
    private Integer idAjusteInventario;

    private Integer cantidad;

    private String motivo;

    private Integer stockAnterior;

    private Integer stockNuevo;

    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private TipoAjusteInventario tipoAjusteInventario;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

}


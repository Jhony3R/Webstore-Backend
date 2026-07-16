package jhony.ruiz.sigevi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tbl_venta")
public class Venta {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @EqualsAndHashCode.Include
    private Integer idVenta;

    private String numeroComprobante;

    private LocalDateTime fecha;

    private Double subTotal;

    private Double valorDescuento;

    private Double total;

    private String motivoAnulacion;

    private LocalDateTime fechaAnulacion;

    @Enumerated(EnumType.STRING)
    private TipoDescuento tipodescuento;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    private EstadoVenta estadoVenta;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_caja", nullable = false)
    private Caja caja;

    @OneToMany(mappedBy="venta",cascade=CascadeType.ALL)
    private List<DetalleVenta> detalles;
}


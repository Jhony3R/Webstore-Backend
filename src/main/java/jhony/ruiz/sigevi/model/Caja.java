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
@Table(name = "tbl_caja")
public class Caja {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @EqualsAndHashCode.Include
    private Integer idCaja;

    private Double saldoInicial;

    private Double saldoEsperadoEfectivo;

    private Double efectivoContado;

    private Double descuadre;

    @Enumerated(EnumType.STRING)
    private EstadoCaja estadoCaja;

    private String observacionDescuadre;

    private Double totalYape;

    private Double totalPlin;

    private Double totalTarjeta;

    private Double totalTransferencia;

    private LocalDateTime fechaApertura;

    private LocalDateTime fechaCierre;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

}


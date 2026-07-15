package jhony.ruiz.sigevi.dto;
import jhony.ruiz.sigevi.model.EstadoCaja;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CajaDTO {
    private Integer idCaja;

    private Double saldoInicial;

    private Double saldoEsperadoEfectivo;

    private Double efectivoContado;

    private Double descuadre;

    private EstadoCaja estadoCaja;

    private String observacionDescuadre;

    private Double totalYape;

    private Double totalPlin;

    private Double totalTarjeta;

    private Double totalTransferencia;

    private LocalDateTime fechaApertura;

    private LocalDateTime fechaCierre;
}

package jhony.ruiz.sigevi.dto.Reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaDiariaDTO {
    private LocalDate fecha;
    private Long cantidadVentas;
    private Double totalEfectivo;
    private Double totalYape;
    private Double totalPlin;
    private Double totalTarjeta;
    private Double totalTransferencia;
    private Double totalGeneral;
}

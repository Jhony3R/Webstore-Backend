package jhony.ruiz.sigevi.dto.Reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GananciaPeriodoDTO {
    private LocalDate fecha;
    private Double totalVentas;
    private Double totalCompras;
    private Double gananciaNeta;
}

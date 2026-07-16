package jhony.ruiz.sigevi.dto.Venta;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnularventarequestDTO {
    @NotBlank(message = "Debes indicar el motivo de la anulación")
    private String motivo;
}

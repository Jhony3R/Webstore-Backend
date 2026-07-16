package jhony.ruiz.sigevi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CierreCajaRequestDTO {

    @NotNull(message = "El efectivo contado es obligatorio")
    @Min(value = 0, message = "El efectivo contado no puede ser negativo")
    private Double efectivoContado;

    private String observacionDescuadre;
}
package jhony.ruiz.sigevi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AperturaCajaRequestDTO {

    @NotNull(message = "El saldo inicial es obligatorio")
    @Min(value = 0, message = "El saldo inicial no puede ser negativo")
    private Double saldoInicial;
}
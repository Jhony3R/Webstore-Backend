package jhony.ruiz.sigevi.dto.AjusteInventario;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jhony.ruiz.sigevi.model.TipoAjusteInventario;
import lombok.Data;

@Data
public class AjusteInventarioRequestDTO {
    @NotNull(message = "El producto es obligatorio")
    private Integer idProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    @NotBlank(message = "El motivo del ajuste es obligatorio")
    private String motivo;

    @NotNull(message = "El tipo de ajuste es obligatorio")
    private TipoAjusteInventario tipoAjusteInventario;
}

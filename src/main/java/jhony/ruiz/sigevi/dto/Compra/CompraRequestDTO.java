package jhony.ruiz.sigevi.dto.Compra;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompraRequestDTO {
    @NotBlank(message = "El número de comprobante es obligatorio")
    private String numeroComprobante;

    private LocalDateTime fecha;

    @NotNull(message = "Debe seleccionar un proveedor")
    private Integer idProveedor;

    @NotNull(message = "Debe indicar el usuario que registra la compra")
    private Integer idUsuario;

    @NotEmpty(message = "Debe agregar al menos un producto a la compra")
    @Valid
    private List<DetalleCompraRequestDTO> detalles;
}

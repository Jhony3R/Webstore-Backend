package jhony.ruiz.sigevi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jhony.ruiz.sigevi.dto.Venta.DetalleVentaRequestDTO;
import jhony.ruiz.sigevi.model.MetodoPago;
import jhony.ruiz.sigevi.model.TipoDescuento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaRequestDTO {

    @NotNull(message = "El cliente es obligatorio")
    private Integer idCliente;

    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodoPago;

    private TipoDescuento tipoDescuento;

    private Double valorDescuentoInput;

    @NotEmpty(message = "Debe registrar al menos un producto en la venta")
    @Valid
    private List<DetalleVentaRequestDTO> detalles;
}
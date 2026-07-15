package jhony.ruiz.sigevi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVentaDTO {
    private Integer idDetalleVenta;

    private Integer cantidad;

    private Double precioUnitario;

    private Double subTotal;

    private VentaDTO venta;

    private ProductoDTO producto;


}


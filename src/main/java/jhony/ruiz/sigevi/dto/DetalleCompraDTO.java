package jhony.ruiz.sigevi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleCompraDTO {
    private Integer idDetalleCompra;

    private Integer cantidad;

    private Double precioUnitario;

    private Double subTotal;

    private ProductoDTO producto;


}


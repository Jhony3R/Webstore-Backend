package jhony.ruiz.sigevi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {
    private Integer idProducto;

    private String codigo;

    private String descripcion;

    private String marca;

    private String talla;

    private String color;

    private Double precioCompra;

    private Double precioVenta;

    private Integer stockActual;

    private Integer stockMinimo;

    private boolean activo;

    private LocalDateTime fechaCreacion;

    private Integer idCategoria;

    private String nombreCategoria;
}


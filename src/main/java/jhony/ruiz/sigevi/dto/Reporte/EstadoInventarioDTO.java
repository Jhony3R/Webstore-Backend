package jhony.ruiz.sigevi.dto.Reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoInventarioDTO {
    private String codigo;
    private String descripcion;
    private String categoria;
    private Integer stockActual;
    private Integer stockMinimo;
    private Double precioVenta;
}

package jhony.ruiz.sigevi.dto.Reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoMasVendidoDTO {
    private String codigo;
    private String descripcion;
    private String categoria;
    private Long cantidadVendida;
    private Double totalIngresos;
}

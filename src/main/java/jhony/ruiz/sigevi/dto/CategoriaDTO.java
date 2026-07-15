package jhony.ruiz.sigevi.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDTO {
    private Integer idCategoria;

    private String nombre;

    private String descripcion;

    private boolean activo;
}

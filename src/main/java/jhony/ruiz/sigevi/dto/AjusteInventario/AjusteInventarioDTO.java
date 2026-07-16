package jhony.ruiz.sigevi.dto.AjusteInventario;
import jhony.ruiz.sigevi.dto.ProductoDTO;
import jhony.ruiz.sigevi.dto.UsuarioDTO;
import jhony.ruiz.sigevi.model.TipoAjusteInventario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AjusteInventarioDTO {
    private Integer idAjusteInventario;

    private Integer cantidad;

    private String motivo;

    private Integer stockAnterior;

    private Integer stockNuevo;

    private LocalDateTime fecha;

    private TipoAjusteInventario tipoAjusteInventario;

    private ProductoDTO producto;

    private UsuarioDTO usuario;
}

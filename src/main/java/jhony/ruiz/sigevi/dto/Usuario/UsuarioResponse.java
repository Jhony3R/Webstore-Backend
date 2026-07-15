package jhony.ruiz.sigevi.dto.Usuario;

import jhony.ruiz.sigevi.model.RolUsuario;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioResponse {
    private Integer idUsuario;
    private String username;
    private String nombreCompleto;
    private String email;
    private RolUsuario rol;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}

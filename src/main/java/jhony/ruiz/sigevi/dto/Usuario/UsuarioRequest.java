package jhony.ruiz.sigevi.dto.Usuario;
import jhony.ruiz.sigevi.model.RolUsuario;
import lombok.Data;

@Data
public class UsuarioRequest {
    private String username;
    private String password;
    private String nombreCompleto;
    private String email;
    private RolUsuario rol;
    private boolean activo;
}
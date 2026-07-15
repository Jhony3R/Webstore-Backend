package jhony.ruiz.sigevi.dto;

import jhony.ruiz.sigevi.model.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Integer idUsuario;

    private String username;

    private String password;

    private String nombreCompleto;

    private String email;

    private RolUsuario rol;

    private boolean activo;

    private LocalDateTime fechaCreacion;

    private LocalDateTime ultimoAcceso;
}


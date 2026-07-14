package jhony.ruiz.sigevi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tbl_usuario")
public class Usuario {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @EqualsAndHashCode.Include
    private Integer idUsuario;

    private String username;

    private String password;

    private String nombreCompleto;

    private String email;

    @Enumerated(EnumType.STRING)
    private RolUsuario rol;

    private boolean activo;

    private LocalDateTime fechaCreacion;

    private LocalDateTime ultimoAcceso;
}


package jhony.ruiz.sigevi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorDTO {
    private Integer idProveedor;

    private String razonSocial;

    private String ruc;

    private String contacto;

    private String email;

    private String telefono;

    private String direccion;

    private boolean activo;
}


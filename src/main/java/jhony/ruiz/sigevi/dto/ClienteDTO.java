package jhony.ruiz.sigevi.dto;
import jhony.ruiz.sigevi.model.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {
    private Integer idCliente;

    private String numeroDocumento;

    private String nombres;

    private String apellidoPaterno;

    private String apellidoMaterno;

    private TipoDocumento tipoDocumento;

    private String telefono;

    private String email;

    private String direccion;

    private LocalDateTime fechaRegistro;
}

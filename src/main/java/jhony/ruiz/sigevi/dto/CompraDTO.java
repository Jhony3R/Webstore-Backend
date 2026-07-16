package jhony.ruiz.sigevi.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompraDTO {
    private Integer idCompra;

    private String numeroComprobante;

    private LocalDateTime fecha;

    private Double montoTotal;

    private ProveedorDTO proveedor;

    private UsuarioDTO usuario;

    private List<DetalleCompraDTO> detalles;

}

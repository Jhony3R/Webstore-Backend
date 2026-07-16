package jhony.ruiz.sigevi.dto;

import jhony.ruiz.sigevi.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaDTO {
    private Integer idVenta;

    private String numeroComprobante;

    private LocalDateTime fecha;

    private Double subTotal;

    private Double valorDescuento;

    private Double total;

    private String motivoAnulacion;

    private LocalDateTime fechaAnulacion;

    private TipoDescuento tipodescuento;

    private MetodoPago metodoPago;

    private EstadoVenta estadoVenta;

    private ClienteDTO cliente;

    private UsuarioDTO usuario;

    private CajaDTO caja;

    private List<DetalleVentaDTO> detalles;
}


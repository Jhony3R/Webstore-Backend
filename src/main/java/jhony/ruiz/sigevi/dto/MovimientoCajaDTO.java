package jhony.ruiz.sigevi.dto;

import jhony.ruiz.sigevi.model.MetodoPago;
import jhony.ruiz.sigevi.model.TipoMovimientoCaja;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoCajaDTO {
    private Integer idMovimientoCaja;

    private String concepto;

    private Double monto;

    private LocalDateTime fecha;

    private TipoMovimientoCaja tipoMovimientoCaja;

    private MetodoPago metodoPago;

    private CajaDTO caja;

    private VentaDTO venta;

}


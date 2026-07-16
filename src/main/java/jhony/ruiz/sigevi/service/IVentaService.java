package jhony.ruiz.sigevi.service;

import jhony.ruiz.sigevi.dto.VentaRequestDTO;
import jhony.ruiz.sigevi.model.Venta;

public interface IVentaService extends ICRUD<Venta, Integer> {
    Venta registrarVenta(String username, VentaRequestDTO requestDTO);
}

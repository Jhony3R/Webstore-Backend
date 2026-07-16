package jhony.ruiz.sigevi.service;

import jhony.ruiz.sigevi.dto.Compra.CompraRequestDTO;
import jhony.ruiz.sigevi.dto.CompraDTO;
import jhony.ruiz.sigevi.model.Compra;

public interface ICompraService extends ICRUD<Compra, Integer> {
    CompraDTO registrarCompra(CompraRequestDTO dto);
}

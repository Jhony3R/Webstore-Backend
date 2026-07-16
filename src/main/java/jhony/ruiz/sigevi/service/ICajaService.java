package jhony.ruiz.sigevi.service;

import jhony.ruiz.sigevi.model.Caja;

import java.util.List;

public interface ICajaService extends ICRUD<Caja, Integer> {
    Caja aperturarCaja(String username, Double saldoInicial);

    Caja buscarCajaAbiertaDeUsuario(String username);

    Caja cerrarCaja(String username, Double efectivoContado, String observacionDescuadre);

    List<Caja> buscarCajasDeUsuario(String username);
}

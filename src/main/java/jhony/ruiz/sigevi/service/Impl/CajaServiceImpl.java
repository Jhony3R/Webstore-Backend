package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.exception.CajaNoAbiertaException;
import jhony.ruiz.sigevi.exception.CajaYaAbiertaException;
import jhony.ruiz.sigevi.exception.ModelNotFoundException;
import jhony.ruiz.sigevi.exception.ObservacionRequeridaException;
import jhony.ruiz.sigevi.model.Caja;
import jhony.ruiz.sigevi.model.EstadoCaja;
import jhony.ruiz.sigevi.model.Usuario;
import jhony.ruiz.sigevi.repository.ICajaRepository;
import jhony.ruiz.sigevi.repository.IGenericRepo;
import jhony.ruiz.sigevi.repository.IUsuarioRepository;
import jhony.ruiz.sigevi.service.ICajaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CajaServiceImpl extends CRUDImpl<Caja, Integer> implements ICajaService {
    private final ICajaRepository cajaRepository;
    private final IUsuarioRepository usuarioRepository;

    @Override
    protected IGenericRepo<Caja, Integer> getRepo() {
        return cajaRepository;
    }

    @Override
    @Transactional
    public Caja aperturarCaja(String username, Double saldoInicial) {

        // RF-06: un usuario no puede tener dos cajas abiertas a la vez
        cajaRepository.findByEstadoCajaAndUsuarioUsername(EstadoCaja.ABIERTA, username)
                .ifPresent(c -> {
                    throw new CajaYaAbiertaException("Ya tienes una caja abierta. Ciérrala antes de abrir otra.");
                });

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado: " + username));

        Caja caja = new Caja();
        caja.setSaldoInicial(saldoInicial);
        caja.setSaldoEsperadoEfectivo(saldoInicial);
        caja.setEfectivoContado(0.0);
        caja.setDescuadre(0.0);
        caja.setEstadoCaja(EstadoCaja.ABIERTA);
        caja.setObservacionDescuadre("");
        caja.setTotalYape(0.0);
        caja.setTotalPlin(0.0);
        caja.setTotalTarjeta(0.0);
        caja.setTotalTransferencia(0.0);
        caja.setFechaApertura(LocalDateTime.now());
        caja.setUsuario(usuario);

        return cajaRepository.save(caja);
    }

    @Override
    @Transactional
    public Caja cerrarCaja(String username, Double efectivoContado, String observacionDescuadre) {

        Caja caja = cajaRepository.findByEstadoCajaAndUsuarioUsername(EstadoCaja.ABIERTA, username)
                .orElseThrow(() -> new CajaNoAbiertaException("No tienes una caja abierta para cerrar."));

        double saldoEsperado = caja.getSaldoEsperadoEfectivo() == null ? 0.0 : caja.getSaldoEsperadoEfectivo();
        double descuadre = Math.round((efectivoContado - saldoEsperado) * 100.0) / 100.0;

        if (descuadre != 0.0 && (observacionDescuadre == null || observacionDescuadre.isBlank())) {
            throw new ObservacionRequeridaException(
                    "Se detectó un descuadre de S/ " + descuadre + ". Debes ingresar una observación antes de cerrar la caja.");
        }

        caja.setEfectivoContado(efectivoContado);
        caja.setDescuadre(descuadre);
        caja.setObservacionDescuadre(observacionDescuadre == null ? "" : observacionDescuadre);
        caja.setEstadoCaja(EstadoCaja.CERRADA);
        caja.setFechaCierre(LocalDateTime.now());

        return cajaRepository.save(caja);
    }

    @Override
    public Caja buscarCajaAbiertaDeUsuario(String username) {
        return cajaRepository.findByEstadoCajaAndUsuarioUsername(EstadoCaja.ABIERTA, username)
                .orElse(null);
    }
}

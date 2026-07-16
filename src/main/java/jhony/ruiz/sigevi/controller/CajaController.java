package jhony.ruiz.sigevi.controller;

import jakarta.validation.Valid;
import jhony.ruiz.sigevi.dto.AperturaCajaRequestDTO;
import jhony.ruiz.sigevi.dto.CajaDTO;
import jhony.ruiz.sigevi.dto.CierreCajaRequestDTO;
import jhony.ruiz.sigevi.model.Caja;
import jhony.ruiz.sigevi.service.ICajaService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/caja")
@RequiredArgsConstructor
public class CajaController {
    private final ICajaService cajaService;
    private final MapperUtil mapperUtil;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<CajaDTO>> findAll() {
        List<CajaDTO> list = mapperUtil.mapList(cajaService.findAll(), CajaDTO.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/mis-cajas")
    public ResponseEntity<List<CajaDTO>> misCajas(Authentication authentication) {
        List<CajaDTO> list = mapperUtil.mapList(
                cajaService.buscarCajasDeUsuario(authentication.getName()), CajaDTO.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CajaDTO> findById(@PathVariable("id") Integer id) {
        Caja obj = cajaService.findById(id);
        return ResponseEntity.ok(mapperUtil.map(obj, CajaDTO.class));
    }

    @GetMapping("/mi-caja-abierta")
    public ResponseEntity<CajaDTO> miCajaAbierta(Authentication authentication) {
        Caja caja = cajaService.buscarCajaAbiertaDeUsuario(authentication.getName());
        if (caja == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(mapperUtil.map(caja, CajaDTO.class));
    }

    @PostMapping("/aperturar")
    public ResponseEntity<CajaDTO> aperturar(@Valid @RequestBody AperturaCajaRequestDTO dto, Authentication authentication) {
        Caja caja = cajaService.aperturarCaja(authentication.getName(), dto.getSaldoInicial());
        CajaDTO cajaDTO = mapperUtil.map(caja, CajaDTO.class);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/api/caja/{id}")
                .buildAndExpand(caja.getIdCaja())
                .toUri();
        return ResponseEntity.created(location).body(cajaDTO);
    }

    @PostMapping("/cerrar")
    public ResponseEntity<CajaDTO> cerrar(@Valid @RequestBody CierreCajaRequestDTO dto, Authentication authentication) {
        Caja caja = cajaService.cerrarCaja(authentication.getName(), dto.getEfectivoContado(), dto.getObservacionDescuadre());
        return ResponseEntity.ok(mapperUtil.map(caja, CajaDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        cajaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

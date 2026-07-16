package jhony.ruiz.sigevi.controller;

import jakarta.validation.Valid;
import jhony.ruiz.sigevi.dto.AjusteInventario.AjusteInventarioDTO;
import jhony.ruiz.sigevi.dto.AjusteInventario.AjusteInventarioRequestDTO;
import jhony.ruiz.sigevi.model.AjusteInventario;
import jhony.ruiz.sigevi.service.IAjusteInventarioService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ajuste-inventario")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AjusteInventarioController {
    private final IAjusteInventarioService ajusteInventarioService;
    private final MapperUtil mapperUtil;

    @GetMapping
    public ResponseEntity<List<AjusteInventarioDTO>> findAll() {
        List<AjusteInventarioDTO> list = mapperUtil.mapList(ajusteInventarioService.findAll(), AjusteInventarioDTO.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AjusteInventarioDTO> findById(@PathVariable("id") Integer id) {
        AjusteInventario obj = ajusteInventarioService.findById(id);
        return ResponseEntity.ok(mapperUtil.map(obj, AjusteInventarioDTO.class));
    }

    @PostMapping
    public ResponseEntity<AjusteInventarioDTO> save(@Valid @RequestBody AjusteInventarioRequestDTO request) {
        AjusteInventario obj = ajusteInventarioService.registrarAjuste(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getIdAjusteInventario()).toUri();
        return ResponseEntity.created(location).body(mapperUtil.map(obj, AjusteInventarioDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        ajusteInventarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
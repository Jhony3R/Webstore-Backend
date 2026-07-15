package jhony.ruiz.sigevi.controller;

import jakarta.validation.Valid;
import jhony.ruiz.sigevi.dto.AjusteInventarioDTO;
import jhony.ruiz.sigevi.model.AjusteInventario;
import jhony.ruiz.sigevi.service.IAjusteInventarioService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/ajuste-inventario")
@RequiredArgsConstructor
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
    public ResponseEntity<Void> save(@Valid @RequestBody AjusteInventarioDTO dto) {
        AjusteInventario obj = ajusteInventarioService.save(mapperUtil.map(dto, AjusteInventario.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdAjusteInventario()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AjusteInventarioDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody AjusteInventarioDTO dto) {
        dto.setIdAjusteInventario(id);
        AjusteInventario obj = ajusteInventarioService.update(id, mapperUtil.map(dto, AjusteInventario.class));
        return ResponseEntity.ok(mapperUtil.map(obj, AjusteInventarioDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        ajusteInventarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

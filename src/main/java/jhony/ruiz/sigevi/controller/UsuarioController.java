package jhony.ruiz.sigevi.controller;

import jakarta.validation.Valid;
import jhony.ruiz.sigevi.dto.UsuarioDTO;
import jhony.ruiz.sigevi.model.Usuario;
import jhony.ruiz.sigevi.service.IUsuarioService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final IUsuarioService usuarioService;
    private final MapperUtil mapperUtil;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        List<UsuarioDTO> list = mapperUtil.mapList(usuarioService.findAll(), UsuarioDTO.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable("id") Integer id) {
        Usuario obj = usuarioService.findById(id);
        return ResponseEntity.ok(mapperUtil.map(obj, UsuarioDTO.class));
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody UsuarioDTO dto) {
        Usuario obj = usuarioService.save(mapperUtil.map(dto, Usuario.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdUsuario()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody UsuarioDTO dto) {
        dto.setIdUsuario(id);
        Usuario obj = usuarioService.update(id, mapperUtil.map(dto, Usuario.class));
        return ResponseEntity.ok(mapperUtil.map(obj, UsuarioDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

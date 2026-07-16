package jhony.ruiz.sigevi.controller;

import jakarta.validation.Valid;
import jhony.ruiz.sigevi.dto.Usuario.UsuarioRequest;
import jhony.ruiz.sigevi.dto.Usuario.UsuarioResponse;
import jhony.ruiz.sigevi.dto.UsuarioDTO;
import jhony.ruiz.sigevi.model.Usuario;
import jhony.ruiz.sigevi.service.IUsuarioService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class UsuarioController {
    private final IUsuarioService usuarioService;
    private final MapperUtil mapperUtil;
    private final PasswordEncoder passwordEncoder;

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

    @GetMapping("/username/{username}")
    public ResponseEntity<UsuarioResponse> findByUsername(@PathVariable String username) {
        Usuario usuario = usuarioService.findByUsername(username);
        return ResponseEntity.ok(mapperUtil.map(usuario, UsuarioResponse.class));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<UsuarioResponse> save(@Valid @RequestBody UsuarioRequest dto) {
        Usuario obj = mapperUtil.map(dto, Usuario.class);
        obj.setPassword(passwordEncoder.encode(dto.getPassword()));
        obj.setActivo(true);
        obj.setFechaCreacion(LocalDateTime.now());

        Usuario saved = usuarioService.save(obj);
        return ResponseEntity.ok(mapperUtil.map(saved, UsuarioResponse.class));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable Integer id, @Valid @RequestBody UsuarioRequest dto) {
        Usuario existente = usuarioService.findById(id);

        existente.setNombreCompleto(dto.getNombreCompleto());
        existente.setEmail(dto.getEmail());
        existente.setRol(dto.getRol());
        existente.setActivo(dto.isActivo());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existente.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Usuario updated = usuarioService.save(existente);
        return ResponseEntity.ok(mapperUtil.map(updated, UsuarioResponse.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Integer id) {
        usuarioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reactivar")
    public ResponseEntity<Void> reactivar(@PathVariable Integer id) {
        usuarioService.reactivar(id);
        return ResponseEntity.ok().build();
    }
}

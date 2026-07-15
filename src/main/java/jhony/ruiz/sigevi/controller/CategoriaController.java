package jhony.ruiz.sigevi.controller;

import jakarta.validation.Valid;
import jhony.ruiz.sigevi.dto.CategoriaDTO;
import jhony.ruiz.sigevi.model.Categoria;
import jhony.ruiz.sigevi.service.ICategoriaService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/categoria")
@RequiredArgsConstructor
public class CategoriaController {
    private final ICategoriaService categoriaService;
    private final MapperUtil mapperUtil;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> findAll() {
        List<CategoriaDTO> list = mapperUtil.mapList(categoriaService.findAll(), CategoriaDTO.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> findById(@PathVariable("id") Integer id) {
        Categoria obj = categoriaService.findById(id);
        return ResponseEntity.ok(mapperUtil.map(obj, CategoriaDTO.class));
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody CategoriaDTO dto) {
        Categoria obj = categoriaService.save(mapperUtil.map(dto, Categoria.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdCategoria()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody CategoriaDTO dto) {
        dto.setIdCategoria(id);
        Categoria obj = categoriaService.update(id, mapperUtil.map(dto, Categoria.class));
        return ResponseEntity.ok(mapperUtil.map(obj, CategoriaDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

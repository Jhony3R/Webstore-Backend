package jhony.ruiz.sigevi.controller;

import jakarta.validation.Valid;
import jhony.ruiz.sigevi.dto.CompraDTO;
import jhony.ruiz.sigevi.model.Compra;
import jhony.ruiz.sigevi.service.ICompraService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/compra")
@RequiredArgsConstructor
public class CompraController {
    private final ICompraService compraService;
    private final MapperUtil mapperUtil;

    @GetMapping
    public ResponseEntity<List<CompraDTO>> findAll() {
        List<CompraDTO> list = mapperUtil.mapList(compraService.findAll(), CompraDTO.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraDTO> findById(@PathVariable("id") Integer id) {
        Compra obj = compraService.findById(id);
        return ResponseEntity.ok(mapperUtil.map(obj, CompraDTO.class));
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody CompraDTO dto) {
        Compra obj = compraService.save(mapperUtil.map(dto, Compra.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdCompra()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompraDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody CompraDTO dto) {
        dto.setIdCompra(id);
        Compra obj = compraService.update(id, mapperUtil.map(dto, Compra.class));
        return ResponseEntity.ok(mapperUtil.map(obj, CompraDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        compraService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

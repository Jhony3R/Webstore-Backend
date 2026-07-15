package jhony.ruiz.sigevi.controller;

import jakarta.validation.Valid;
import jhony.ruiz.sigevi.dto.ProductoDTO;
import jhony.ruiz.sigevi.model.Producto;
import jhony.ruiz.sigevi.service.IProductoService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/producto")
@RequiredArgsConstructor
public class ProductoController {
    private final IProductoService productoService;
    private final MapperUtil mapperUtil;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> findAll() {
        List<ProductoDTO> list = mapperUtil.mapList(productoService.findAll(), ProductoDTO.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> findById(@PathVariable("id") Integer id) {
        Producto obj = productoService.findById(id);
        return ResponseEntity.ok(mapperUtil.map(obj, ProductoDTO.class));
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ProductoDTO dto) {
        Producto obj = productoService.save(mapperUtil.map(dto, Producto.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdProducto()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody ProductoDTO dto) {
        dto.setIdProducto(id);
        Producto obj = productoService.update(id, mapperUtil.map(dto, Producto.class));
        return ResponseEntity.ok(mapperUtil.map(obj, ProductoDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

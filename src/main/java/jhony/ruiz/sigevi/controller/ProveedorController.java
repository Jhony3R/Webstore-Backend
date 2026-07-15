package jhony.ruiz.sigevi.controller;

import jakarta.validation.Valid;
import jhony.ruiz.sigevi.dto.ProveedorDTO;
import jhony.ruiz.sigevi.model.Proveedor;
import jhony.ruiz.sigevi.service.IProveedorService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/proveedor")
@RequiredArgsConstructor
public class ProveedorController {
    private final IProveedorService proveedorService;
    private final MapperUtil mapperUtil;

    @GetMapping
    public ResponseEntity<List<ProveedorDTO>> findAll() {
        List<ProveedorDTO> list = mapperUtil.mapList(proveedorService.findAll(), ProveedorDTO.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDTO> findById(@PathVariable("id") Integer id) {
        Proveedor obj = proveedorService.findById(id);
        return ResponseEntity.ok(mapperUtil.map(obj, ProveedorDTO.class));
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ProveedorDTO dto) {
        Proveedor obj = proveedorService.save(mapperUtil.map(dto, Proveedor.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdProveedor()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody ProveedorDTO dto) {
        dto.setIdProveedor(id);
        Proveedor obj = proveedorService.update(id, mapperUtil.map(dto, Proveedor.class));
        return ResponseEntity.ok(mapperUtil.map(obj, ProveedorDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        proveedorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

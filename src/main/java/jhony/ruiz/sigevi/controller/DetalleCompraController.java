package jhony.ruiz.sigevi.controller;

import jakarta.validation.Valid;
import jhony.ruiz.sigevi.dto.DetalleCompraDTO;
import jhony.ruiz.sigevi.model.DetalleCompra;
import jhony.ruiz.sigevi.service.IDetalleCompraService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/detalle-compra")
@RequiredArgsConstructor
public class DetalleCompraController {
    private final IDetalleCompraService detalleCompraService;
    private final MapperUtil mapperUtil;

    @GetMapping
    public ResponseEntity<List<DetalleCompraDTO>> findAll() {
        List<DetalleCompraDTO> list = mapperUtil.mapList(detalleCompraService.findAll(), DetalleCompraDTO.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleCompraDTO> findById(@PathVariable("id") Integer id) {
        DetalleCompra obj = detalleCompraService.findById(id);
        return ResponseEntity.ok(mapperUtil.map(obj, DetalleCompraDTO.class));
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody DetalleCompraDTO dto) {
        DetalleCompra obj = detalleCompraService.save(mapperUtil.map(dto, DetalleCompra.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdDetalleCompra()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleCompraDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody DetalleCompraDTO dto) {
        dto.setIdDetalleCompra(id);
        DetalleCompra obj = detalleCompraService.update(id, mapperUtil.map(dto, DetalleCompra.class));
        return ResponseEntity.ok(mapperUtil.map(obj, DetalleCompraDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        detalleCompraService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

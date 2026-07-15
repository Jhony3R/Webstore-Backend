package jhony.ruiz.sigevi.controller;

import jakarta.validation.Valid;
import jhony.ruiz.sigevi.dto.DetalleVentaDTO;
import jhony.ruiz.sigevi.model.DetalleVenta;
import jhony.ruiz.sigevi.service.IDetalleVentaService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/detalle-venta")
@RequiredArgsConstructor
public class DetalleVentaController {
    private final IDetalleVentaService detalleVentaService;
    private final MapperUtil mapperUtil;

    @GetMapping
    public ResponseEntity<List<DetalleVentaDTO>> findAll() {
        List<DetalleVentaDTO> list = mapperUtil.mapList(detalleVentaService.findAll(), DetalleVentaDTO.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVentaDTO> findById(@PathVariable("id") Integer id) {
        DetalleVenta obj = detalleVentaService.findById(id);
        return ResponseEntity.ok(mapperUtil.map(obj, DetalleVentaDTO.class));
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody DetalleVentaDTO dto) {
        DetalleVenta obj = detalleVentaService.save(mapperUtil.map(dto, DetalleVenta.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdDetalleVenta()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleVentaDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody DetalleVentaDTO dto) {
        dto.setIdDetalleVenta(id);
        DetalleVenta obj = detalleVentaService.update(id, mapperUtil.map(dto, DetalleVenta.class));
        return ResponseEntity.ok(mapperUtil.map(obj, DetalleVentaDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        detalleVentaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package jhony.ruiz.sigevi.controller;

import jakarta.validation.Valid;
import jhony.ruiz.sigevi.dto.MovimientoCajaDTO;
import jhony.ruiz.sigevi.model.MovimientoCaja;
import jhony.ruiz.sigevi.service.IMovimientoCajaService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/movimiento-caja")
@RequiredArgsConstructor
public class MovimientoCajaController {
    private final IMovimientoCajaService movimientoCajaService;
    private final MapperUtil mapperUtil;

    @GetMapping
    public ResponseEntity<List<MovimientoCajaDTO>> findAll() {
        List<MovimientoCajaDTO> list = mapperUtil.mapList(movimientoCajaService.findAll(), MovimientoCajaDTO.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoCajaDTO> findById(@PathVariable("id") Integer id) {
        MovimientoCaja obj = movimientoCajaService.findById(id);
        return ResponseEntity.ok(mapperUtil.map(obj, MovimientoCajaDTO.class));
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody MovimientoCajaDTO dto) {
        MovimientoCaja obj = movimientoCajaService.save(mapperUtil.map(dto, MovimientoCaja.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdMovimientoCaja()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoCajaDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody MovimientoCajaDTO dto) {
        dto.setIdMovimientoCaja(id);
        MovimientoCaja obj = movimientoCajaService.update(id, mapperUtil.map(dto, MovimientoCaja.class));
        return ResponseEntity.ok(mapperUtil.map(obj, MovimientoCajaDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        movimientoCajaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package jhony.ruiz.sigevi.controller;

import jakarta.validation.Valid;
import jhony.ruiz.sigevi.dto.CajaDTO;
import jhony.ruiz.sigevi.model.Caja;
import jhony.ruiz.sigevi.service.ICajaService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/caja")
@RequiredArgsConstructor
public class CajaController {
    private final ICajaService cajaService;
    private final MapperUtil mapperUtil;

    @GetMapping
    public ResponseEntity<List<CajaDTO>> findAll() {
        List<CajaDTO> list = mapperUtil.mapList(cajaService.findAll(), CajaDTO.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CajaDTO> findById(@PathVariable("id") Integer id) {
        Caja obj = cajaService.findById(id);
        return ResponseEntity.ok(mapperUtil.map(obj, CajaDTO.class));
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody CajaDTO dto) {
        Caja obj = cajaService.save(mapperUtil.map(dto, Caja.class));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdCaja()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CajaDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody CajaDTO dto) {
        dto.setIdCaja(id);
        Caja obj = cajaService.update(id, mapperUtil.map(dto, Caja.class));
        return ResponseEntity.ok(mapperUtil.map(obj, CajaDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        cajaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

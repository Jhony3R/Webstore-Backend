package jhony.ruiz.sigevi.controller;

import jakarta.validation.Valid;
import jhony.ruiz.sigevi.dto.VentaDTO;
import jhony.ruiz.sigevi.dto.VentaRequestDTO;
import jhony.ruiz.sigevi.model.Venta;
import jhony.ruiz.sigevi.service.IVentaService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/venta")
@RequiredArgsConstructor
public class VentaController {
    private final IVentaService ventaService;
    private final MapperUtil mapperUtil;

    @GetMapping
    public ResponseEntity<List<VentaDTO>> findAll() {
        List<VentaDTO> list = mapperUtil.mapList(ventaService.findAll(), VentaDTO.class);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> findById(@PathVariable("id") Integer id) {
        Venta obj = ventaService.findById(id);
        return ResponseEntity.ok(mapperUtil.map(obj, VentaDTO.class));
    }

    @PostMapping("/registrar")
    public ResponseEntity<VentaDTO> registrarVenta(@Valid @RequestBody VentaRequestDTO requestDTO, Authentication authentication) {
        Venta ventaGuardada = ventaService.registrarVenta(authentication.getName(), requestDTO);
        VentaDTO dto = mapperUtil.map(ventaGuardada, VentaDTO.class);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/api/venta/{id}")
                .buildAndExpand(ventaGuardada.getIdVenta())
                .toUri();
        return ResponseEntity.created(location).body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        ventaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

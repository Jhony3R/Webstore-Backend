package jhony.ruiz.sigevi.controller;

import jakarta.validation.Valid;
import jhony.ruiz.sigevi.dto.Venta.AnularventarequestDTO;
import jhony.ruiz.sigevi.dto.VentaDTO;
import jhony.ruiz.sigevi.dto.VentaRequestDTO;
import jhony.ruiz.sigevi.model.Venta;
import jhony.ruiz.sigevi.service.IVentaService;
import jhony.ruiz.sigevi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/{id}/comprobante")
    public ResponseEntity<byte[]> descargarComprobante(@PathVariable("id") Integer id) {
        byte[] pdf = ventaService.generarComprobantePdf(id);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=comprobante-" + id + ".pdf")
                .body(pdf);
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

    @PutMapping("/{id}/anular")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<VentaDTO> anular(@PathVariable("id") Integer id,
                                           @Valid @RequestBody AnularventarequestDTO requestDTO) {
        Venta ventaAnulada = ventaService.anularVenta(id, requestDTO.getMotivo());
        return ResponseEntity.ok(mapperUtil.map(ventaAnulada, VentaDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        ventaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

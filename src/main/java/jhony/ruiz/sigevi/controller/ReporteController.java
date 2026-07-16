package jhony.ruiz.sigevi.controller;

import jhony.ruiz.sigevi.dto.Reporte.*;
import jhony.ruiz.sigevi.service.IReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/reporte")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class ReporteController {
    private final IReporteService reporteService;

    @GetMapping("/ventas-diarias")
    public ResponseEntity<List<VentaDiariaDTO>> ventasDiarias(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(reporteService.ventasDiarias(fechaInicio, fechaFin));
    }

    @GetMapping("/productos-mas-vendidos")
    public ResponseEntity<List<ProductoMasVendidoDTO>> productosMasVendidos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(reporteService.productosMasVendidos(fechaInicio, fechaFin));
    }

    @GetMapping("/ganancias")
    public ResponseEntity<List<GananciaPeriodoDTO>> ganancias(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(reporteService.ganancias(fechaInicio, fechaFin));
    }

    @GetMapping("/inventario")
    public ResponseEntity<List<EstadoInventarioDTO>> estadoInventario() {
        return ResponseEntity.ok(reporteService.estadoInventario());
    }

    @GetMapping("/exportar/{tipoReporte}")
    public ResponseEntity<byte[]> exportar(
            @PathVariable String tipoReporte,
            @RequestParam String formato,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        byte[] contenido;
        MediaType mediaType;
        String extension;

        if ("excel".equalsIgnoreCase(formato)) {
            contenido = reporteService.exportarExcel(tipoReporte, fechaInicio, fechaFin);
            mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            extension = "xlsx";
        } else {
            contenido = reporteService.exportarPdf(tipoReporte, fechaInicio, fechaFin);
            mediaType = MediaType.APPLICATION_PDF;
            extension = "pdf";
        }

        String nombreArchivo = "reporte-" + tipoReporte + "." + extension;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                .body(contenido);
    }
}
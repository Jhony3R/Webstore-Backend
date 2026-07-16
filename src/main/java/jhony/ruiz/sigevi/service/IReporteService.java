package jhony.ruiz.sigevi.service;

import jhony.ruiz.sigevi.dto.Reporte.*;

import java.time.LocalDate;
import java.util.List;

public interface IReporteService {
    List<VentaDiariaDTO> ventasDiarias(LocalDate fechaInicio, LocalDate fechaFin);
    List<ProductoMasVendidoDTO> productosMasVendidos(LocalDate fechaInicio, LocalDate fechaFin);
    List<GananciaPeriodoDTO> ganancias(LocalDate fechaInicio, LocalDate fechaFin);
    List<EstadoInventarioDTO> estadoInventario();

    byte[] exportarExcel(String tipoReporte, LocalDate fechaInicio, LocalDate fechaFin);
    byte[] exportarPdf(String tipoReporte, LocalDate fechaInicio, LocalDate fechaFin);
}
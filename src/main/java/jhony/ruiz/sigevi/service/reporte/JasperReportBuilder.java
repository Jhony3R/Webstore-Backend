package jhony.ruiz.sigevi.service.reporte;

import jhony.ruiz.sigevi.dto.Reporte.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JasperReportBuilder {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] ventasDiarias(List<VentaDiariaDTO> datos, LocalDate fechaInicio, LocalDate fechaFin) {
        return generar("reports/ventas_diarias.jrxml", datos,
                paramsRango("Reporte de Ventas Diarias", fechaInicio, fechaFin));
    }

    public byte[] productosMasVendidos(List<ProductoMasVendidoDTO> datos, LocalDate fechaInicio, LocalDate fechaFin) {
        return generar("reports/productos_mas_vendidos.jrxml", datos,
                paramsRango("Productos Más Vendidos", fechaInicio, fechaFin));
    }

    public byte[] ganancias(List<GananciaPeriodoDTO> datos, LocalDate fechaInicio, LocalDate fechaFin) {
        return generar("reports/ganancias.jrxml", datos,
                paramsRango("Reporte de Ganancias Netas", fechaInicio, fechaFin));
    }

    public byte[] estadoInventario(List<EstadoInventarioDTO> datos) {
        Map<String, Object> params = new HashMap<>();
        params.put("titulo", "Estado del Inventario");
        params.put("rango", "Al " + LocalDate.now().format(FORMATO_FECHA));
        return generar("reports/estado_inventario.jrxml", datos, params);
    }

    private Map<String, Object> paramsRango(String titulo, LocalDate fechaInicio, LocalDate fechaFin) {
        Map<String, Object> params = new HashMap<>();
        params.put("titulo", titulo);
        params.put("rango", "Del " + fechaInicio.format(FORMATO_FECHA) + " al " + fechaFin.format(FORMATO_FECHA));
        return params;
    }

    private <T> byte[] generar(String rutaPlantilla, List<T> datos, Map<String, Object> parametros) {
        try {
            InputStream jrxmlStream = new ClassPathResource(rutaPlantilla).getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datos);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new RuntimeException("Error generando el PDF con JasperReports: " + rutaPlantilla, e);
        }
    }
}
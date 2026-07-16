package jhony.ruiz.sigevi.dto.Reporte;

import jhony.ruiz.sigevi.dto.Reporte.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class ExcelReportBuilder {

    public byte[] ventasDiarias(List<VentaDiariaDTO> datos) {
        String[] cabeceras = {"Fecha", "N° Ventas", "Efectivo", "Yape", "Plin", "Tarjeta", "Transferencia", "Total"};
        return construir("Ventas Diarias", cabeceras, datos, (fila, d) -> {
            fila.createCell(0).setCellValue(d.getFecha().toString());
            fila.createCell(1).setCellValue(d.getCantidadVentas());
            fila.createCell(2).setCellValue(d.getTotalEfectivo());
            fila.createCell(3).setCellValue(d.getTotalYape());
            fila.createCell(4).setCellValue(d.getTotalPlin());
            fila.createCell(5).setCellValue(d.getTotalTarjeta());
            fila.createCell(6).setCellValue(d.getTotalTransferencia());
            fila.createCell(7).setCellValue(d.getTotalGeneral());
        });
    }

    public byte[] productosMasVendidos(List<ProductoMasVendidoDTO> datos) {
        String[] cabeceras = {"Código", "Producto", "Categoría", "Cant. Vendida", "Ingresos"};
        return construir("Productos Más Vendidos", cabeceras, datos, (fila, d) -> {
            fila.createCell(0).setCellValue(d.getCodigo());
            fila.createCell(1).setCellValue(d.getDescripcion());
            fila.createCell(2).setCellValue(d.getCategoria() == null ? "" : d.getCategoria());
            fila.createCell(3).setCellValue(d.getCantidadVendida());
            fila.createCell(4).setCellValue(d.getTotalIngresos());
        });
    }

    public byte[] ganancias(List<GananciaPeriodoDTO> datos) {
        String[] cabeceras = {"Fecha", "Ventas", "Compras", "Ganancia Neta"};
        return construir("Ganancias", cabeceras, datos, (fila, d) -> {
            fila.createCell(0).setCellValue(d.getFecha().toString());
            fila.createCell(1).setCellValue(d.getTotalVentas());
            fila.createCell(2).setCellValue(d.getTotalCompras());
            fila.createCell(3).setCellValue(d.getGananciaNeta());
        });
    }

    public byte[] estadoInventario(List<EstadoInventarioDTO> datos) {
        String[] cabeceras = {"Código", "Producto", "Categoría", "P. Venta", "Stock Actual", "Stock Mínimo"};
        return construir("Estado de Inventario", cabeceras, datos, (fila, d) -> {
            fila.createCell(0).setCellValue(d.getCodigo());
            fila.createCell(1).setCellValue(d.getDescripcion());
            fila.createCell(2).setCellValue(d.getCategoria() == null ? "" : d.getCategoria());
            fila.createCell(3).setCellValue(d.getPrecioVenta());
            fila.createCell(4).setCellValue(d.getStockActual());
            fila.createCell(5).setCellValue(d.getStockMinimo());
        });
    }

    private <T> byte[] construir(String nombreHoja, String[] cabeceras, List<T> datos, FilaEscritor<T> escritor) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(nombreHoja);

            CellStyle estiloCabecera = workbook.createCellStyle();
            Font fuente = workbook.createFont();
            fuente.setBold(true);
            estiloCabecera.setFont(fuente);

            Row filaCabecera = sheet.createRow(0);
            for (int i = 0; i < cabeceras.length; i++) {
                Cell cell = filaCabecera.createCell(i);
                cell.setCellValue(cabeceras[i]);
                cell.setCellStyle(estiloCabecera);
            }

            int numeroFila = 1;
            for (T dato : datos) {
                Row fila = sheet.createRow(numeroFila++);
                escritor.escribir(fila, dato);
            }

            for (int i = 0; i < cabeceras.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando el archivo Excel", e);
        }
    }

    @FunctionalInterface
    private interface FilaEscritor<T> {
        void escribir(Row fila, T dato);
    }
}

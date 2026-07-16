package jhony.ruiz.sigevi.service.Impl;

import jhony.ruiz.sigevi.dto.Reporte.*;
import jhony.ruiz.sigevi.model.MetodoPago;
import jhony.ruiz.sigevi.repository.ICompraRepository;
import jhony.ruiz.sigevi.repository.IDetalleVentaRepository;
import jhony.ruiz.sigevi.repository.IProductoRepository;
import jhony.ruiz.sigevi.repository.IVentaRepository;
import jhony.ruiz.sigevi.service.IReporteService;
import jhony.ruiz.sigevi.service.reporte.JasperReportBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements IReporteService {

    private final IVentaRepository ventaRepository;
    private final IDetalleVentaRepository detalleVentaRepository;
    private final ICompraRepository compraRepository;
    private final IProductoRepository productoRepository;
    private final ExcelReportBuilder excelReportBuilder;
    private final JasperReportBuilder jasperReportBuilder;

    @Override
    public List<VentaDiariaDTO> ventasDiarias(LocalDate fechaInicio, LocalDate fechaFin) {
        Map<LocalDate, VentaDiariaDTO> porFecha = new TreeMap<>();

        for (Object[] fila : ventaRepository.ventasAgrupadasPorFechaYMetodo(fechaInicio, fechaFin)) {
            LocalDate fecha = ((java.sql.Date) fila[0]).toLocalDate();
            MetodoPago metodoPago = (MetodoPago) fila[1];
            Long cantidad = (Long) fila[2];
            Double total = (Double) fila[3];

            VentaDiariaDTO dto = porFecha.computeIfAbsent(fecha, f -> new VentaDiariaDTO(
                    f, 0L, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));

            dto.setCantidadVentas(dto.getCantidadVentas() + cantidad);
            dto.setTotalGeneral(dto.getTotalGeneral() + total);

            switch (metodoPago) {
                case EFECTIVO -> dto.setTotalEfectivo(dto.getTotalEfectivo() + total);
                case YAPE -> dto.setTotalYape(dto.getTotalYape() + total);
                case PLIN -> dto.setTotalPlin(dto.getTotalPlin() + total);
                case TARJETA -> dto.setTotalTarjeta(dto.getTotalTarjeta() + total);
                case TRANSFERENCIA -> dto.setTotalTransferencia(dto.getTotalTransferencia() + total);
            }
        }

        return new ArrayList<>(porFecha.values());
    }

    @Override
    public List<ProductoMasVendidoDTO> productosMasVendidos(LocalDate fechaInicio, LocalDate fechaFin) {
        return detalleVentaRepository.reporteProductosMasVendidos(fechaInicio, fechaFin);
    }

    @Override
    public List<GananciaPeriodoDTO> ganancias(LocalDate fechaInicio, LocalDate fechaFin) {
        Map<LocalDate, Double> ventasPorFecha = new TreeMap<>();
        Map<LocalDate, Double> comprasPorFecha = new TreeMap<>();

        for (Object[] fila : ventaRepository.sumVentasPorFecha(fechaInicio, fechaFin)) {
            LocalDate fecha = ((java.sql.Date) fila[0]).toLocalDate();
            ventasPorFecha.put(fecha, (Double) fila[1]);
        }
        for (Object[] fila : compraRepository.sumComprasPorFecha(fechaInicio, fechaFin)) {
            LocalDate fecha = ((java.sql.Date) fila[0]).toLocalDate();
            comprasPorFecha.put(fecha, (Double) fila[1]);
        }

        Set<LocalDate> fechas = new TreeSet<>();
        fechas.addAll(ventasPorFecha.keySet());
        fechas.addAll(comprasPorFecha.keySet());

        List<GananciaPeriodoDTO> resultado = new ArrayList<>();
        for (LocalDate fecha : fechas) {
            double totalVentas = ventasPorFecha.getOrDefault(fecha, 0.0);
            double totalCompras = comprasPorFecha.getOrDefault(fecha, 0.0);
            resultado.add(new GananciaPeriodoDTO(fecha, totalVentas, totalCompras, totalVentas - totalCompras));
        }
        return resultado;
    }

    @Override
    public List<EstadoInventarioDTO> estadoInventario() {
        return productoRepository.reporteEstadoInventario();
    }

    @Override
    public byte[] exportarExcel(String tipoReporte, LocalDate fechaInicio, LocalDate fechaFin) {
        return switch (tipoReporte) {
            case "ventas" -> excelReportBuilder.ventasDiarias(ventasDiarias(fechaInicio, fechaFin));
            case "productos" -> excelReportBuilder.productosMasVendidos(productosMasVendidos(fechaInicio, fechaFin));
            case "ganancias" -> excelReportBuilder.ganancias(ganancias(fechaInicio, fechaFin));
            case "inventario" -> excelReportBuilder.estadoInventario(estadoInventario());
            default -> throw new IllegalArgumentException("Tipo de reporte no soportado: " + tipoReporte);
        };
    }

    @Override
    public byte[] exportarPdf(String tipoReporte, LocalDate fechaInicio, LocalDate fechaFin) {
        return switch (tipoReporte) {
            case "ventas" -> jasperReportBuilder.ventasDiarias(ventasDiarias(fechaInicio, fechaFin), fechaInicio, fechaFin);
            case "productos" -> jasperReportBuilder.productosMasVendidos(productosMasVendidos(fechaInicio, fechaFin), fechaInicio, fechaFin);
            case "ganancias" -> jasperReportBuilder.ganancias(ganancias(fechaInicio, fechaFin), fechaInicio, fechaFin);
            case "inventario" -> jasperReportBuilder.estadoInventario(estadoInventario());
            default -> throw new IllegalArgumentException("Tipo de reporte no soportado: " + tipoReporte);
        };
    }
}
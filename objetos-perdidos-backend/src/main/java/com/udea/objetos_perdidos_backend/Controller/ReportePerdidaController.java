package com.udea.objetos_perdidos_backend.Controller;

import com.udea.objetos_perdidos_backend.Dto.ReportePerdidaRequest;
import com.udea.objetos_perdidos_backend.Model.ReportePerdida;
import com.udea.objetos_perdidos_backend.Service.ReportePerdidaService;
import org.springframework.web.bind.annotation.*;
import com.udea.objetos_perdidos_backend.Dto.ReporteAdminDTO;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReportePerdidaController {

    private final ReportePerdidaService reporteService;

    public ReportePerdidaController(ReportePerdidaService reporteService) {
        this.reporteService = reporteService;
    }
    @GetMapping("/admin")
    public List<ReporteAdminDTO> listarReportesAdmin() {
        return reporteService.listarReportesAdmin();
    }
    @PostMapping
    public ReportePerdida crearReporte(@RequestBody ReportePerdidaRequest request) {
        return reporteService.crearReporte(request);
    }
}
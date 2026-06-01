package com.udea.objetos_perdidos_backend.Service;

import com.udea.objetos_perdidos_backend.Dto.ReportePerdidaRequest;
import com.udea.objetos_perdidos_backend.Model.Persona;
import com.udea.objetos_perdidos_backend.Model.ReportePerdida;
import com.udea.objetos_perdidos_backend.Repository.PersonaRepository;
import com.udea.objetos_perdidos_backend.Repository.ReportePerdidaRepository;
import org.springframework.stereotype.Service;
import com.udea.objetos_perdidos_backend.Dto.ReporteAdminDTO;
import com.udea.objetos_perdidos_backend.Repository.ReporteAdminProjection;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class ReportePerdidaService {

    private static final int ESTADO_REPORTE_PENDIENTE = 6;
    private static final int ESTADO_REPORTE_RESUELTO = 7;

    private final ReportePerdidaRepository reporteRepository;
    private final PersonaRepository personaRepository;

    public ReportePerdidaService(
            ReportePerdidaRepository reporteRepository,
            PersonaRepository personaRepository
    ) {
        this.reporteRepository = reporteRepository;
        this.personaRepository = personaRepository;
    }

    public ReportePerdida crearReporte(ReportePerdidaRequest request) {
        Persona persona = personaRepository
                .findByCorreo(request.getCorreoUsuario().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("No existe una persona con ese correo"));

        ReportePerdida reporte = new ReportePerdida();
        reporte.setDescripcionObjeto(request.getDescripcionObjeto());
        reporte.setFechaReporte(LocalDateTime.now());
        reporte.setFechaAproxPerdida(request.getFechaAproxPerdida());
        reporte.setIdLugarAproxPerdida(request.getIdLugarAproxPerdida());
        reporte.setIdPersona(persona.getId());
        reporte.setIdEstado(ESTADO_REPORTE_PENDIENTE);

        return reporteRepository.save(reporte);
    }
    public List<ReporteAdminDTO> listarReportesAdmin() {
        List<ReporteAdminProjection> reportes = reporteRepository.listarReportesAdmin();

        return reportes.stream()
                .map(r -> new ReporteAdminDTO(
                        r.getId(),
                        r.getDescripcionObjeto(),
                        r.getFechaReporte(),
                        r.getFechaAproxPerdida(),
                        r.getIdEstado(),
                        r.getCorreoUsuario(),
                        r.getNombreUsuario(),
                        r.getLugar()
                ))
                .toList();
    }
}
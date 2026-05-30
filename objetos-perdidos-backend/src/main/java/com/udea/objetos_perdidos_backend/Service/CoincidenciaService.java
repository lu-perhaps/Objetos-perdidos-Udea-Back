package com.udea.objetos_perdidos_backend.Service;

import com.udea.objetos_perdidos_backend.Dto.CoincidenciaRequest;
import com.udea.objetos_perdidos_backend.Model.Lugar;
import com.udea.objetos_perdidos_backend.Model.Notificacion;
import com.udea.objetos_perdidos_backend.Model.Objeto;
import com.udea.objetos_perdidos_backend.Model.ReportePerdida;
import com.udea.objetos_perdidos_backend.Model.SolicitudReclamo;
import com.udea.objetos_perdidos_backend.Repository.LugarRepository;
import com.udea.objetos_perdidos_backend.Repository.NotificacionRepository;
import com.udea.objetos_perdidos_backend.Repository.ObjetoRepository;
import com.udea.objetos_perdidos_backend.Repository.ReportePerdidaRepository;
import com.udea.objetos_perdidos_backend.Repository.SolicitudReclamoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CoincidenciaService {

    private static final int ESTADO_REPORTE_RESUELTO = 7;
    private static final int ESTADO_SOLICITUD_APROBADA = 9;
    private static final int TIPO_NOTIFICACION_OBJETO_ENCONTRADO = 4;

    private final ReportePerdidaRepository reporteRepository;
    private final ObjetoRepository objetoRepository;
    private final SolicitudReclamoRepository solicitudRepository;
    private final NotificacionRepository notificacionRepository;
    private final LugarRepository lugarRepository;

    public CoincidenciaService(
            ReportePerdidaRepository reporteRepository,
            ObjetoRepository objetoRepository,
            SolicitudReclamoRepository solicitudRepository,
            NotificacionRepository notificacionRepository,
            LugarRepository lugarRepository
    ) {
        this.reporteRepository = reporteRepository;
        this.objetoRepository = objetoRepository;
        this.solicitudRepository = solicitudRepository;
        this.notificacionRepository = notificacionRepository;
        this.lugarRepository = lugarRepository;
    }

    public void procesarCoincidencia(CoincidenciaRequest request) {
        ReportePerdida reporte = reporteRepository.findById(request.getIdReporte())
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        if (reporte.getIdEstado() == ESTADO_REPORTE_RESUELTO) {
            throw new RuntimeException("Este reporte ya fue resuelto");
        }

        Objeto objeto = objetoRepository.findById(request.getIdObjeto())
                .orElseThrow(() -> new RuntimeException("Objeto no encontrado"));

        Optional<SolicitudReclamo> solicitudExistente =
                solicitudRepository.findByIdReporte(reporte.getId());

        SolicitudReclamo solicitud = solicitudExistente.orElseGet(SolicitudReclamo::new);

        solicitud.setDescripcion(reporte.getDescripcionObjeto());
        solicitud.setFecha(LocalDateTime.now());
        solicitud.setIdObjeto(objeto.getId());
        solicitud.setIdPersona(reporte.getIdPersona());
        solicitud.setIdReporte(reporte.getId());
        solicitud.setIdLugarAproxPerdida(reporte.getIdLugarAproxPerdida());
        solicitud.setFechaAproxPerdida(reporte.getFechaAproxPerdida());
        solicitud.setIdEstado(ESTADO_SOLICITUD_APROBADA);

        solicitudRepository.save(solicitud);

        reporte.setIdEstado(ESTADO_REPORTE_RESUELTO);
        reporteRepository.save(reporte);

        String mensajeExtra = request.getMensajePersonalizado() == null
                ? ""
                : request.getMensajePersonalizado().trim();

        String lugarActual = "lugar de custodia registrado";

        if (objeto.getIdLugarActual() != null) {
            lugarActual = lugarRepository.findById(objeto.getIdLugarActual())
                    .map(Lugar::getNombre)
                    .orElse("lugar de custodia registrado");
        }

        String mensaje = "Encontramos una coincidencia con tu reporte de pérdida. "
                + "Objeto: " + objeto.getNombre() + ". "
                + "Dirígete a: " + lugarActual + ".";

        if (!mensajeExtra.isEmpty()) {
            mensaje += " " + mensajeExtra;
        }

        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(mensaje);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setIdPersonaRecibe(reporte.getIdPersona());
        notificacion.setIdPersonaEnvia(null);
        notificacion.setIdTipoNotificacion(TIPO_NOTIFICACION_OBJETO_ENCONTRADO);

        notificacionRepository.save(notificacion);
    }
}
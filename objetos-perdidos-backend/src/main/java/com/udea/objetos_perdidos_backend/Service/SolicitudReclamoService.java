package com.udea.objetos_perdidos_backend.Service;

import com.udea.objetos_perdidos_backend.Dto.SolicitudAdminDTO;
import com.udea.objetos_perdidos_backend.Dto.SolicitudReclamoRequest;
import com.udea.objetos_perdidos_backend.Model.Persona;
import com.udea.objetos_perdidos_backend.Model.SolicitudReclamo;
import com.udea.objetos_perdidos_backend.Repository.PersonaRepository;
import com.udea.objetos_perdidos_backend.Repository.SolicitudAdminProjection;
import com.udea.objetos_perdidos_backend.Repository.SolicitudReclamoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitudReclamoService {

    private static final int ESTADO_PENDIENTE = 8;
    private static final int ESTADO_APROBADO = 9;
    private static final int ESTADO_RECHAZADO = 10;

    private final SolicitudReclamoRepository solicitudRepository;
    private final PersonaRepository personaRepository;

    public SolicitudReclamoService(
            SolicitudReclamoRepository solicitudRepository,
            PersonaRepository personaRepository
    ) {
        this.solicitudRepository = solicitudRepository;
        this.personaRepository = personaRepository;
    }

    public SolicitudReclamo crearSolicitud(SolicitudReclamoRequest request) {
        Persona persona = personaRepository
                .findByCorreo(request.getCorreoUsuario().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("No existe una persona con ese correo"));

        SolicitudReclamo solicitud = new SolicitudReclamo();
        solicitud.setDescripcion(request.getDescripcion());
        solicitud.setFecha(LocalDateTime.now());
        solicitud.setIdObjeto(request.getIdObjeto());
        solicitud.setIdPersona(persona.getId());
        solicitud.setIdLugarAproxPerdida(request.getIdLugarAproxPerdida());
        solicitud.setFechaAproxPerdida(request.getFechaAproxPerdida());
        solicitud.setIdEstado(ESTADO_PENDIENTE);

        return solicitudRepository.save(solicitud);
    }

    public List<SolicitudAdminDTO> listarSolicitudesAdmin() {
        List<SolicitudAdminProjection> solicitudes =
                solicitudRepository.listarSolicitudesAdmin();

        return solicitudes.stream()
                .map(s -> new SolicitudAdminDTO(
                        s.getId(),
                        s.getDescripcion(),
                        s.getFecha(),
                        s.getFechaAproxPerdida(),
                        s.getIdEstado(),
                        s.getObjeto(),
                        s.getCorreoUsuario(),
                        s.getLugar()
                ))
                .toList();
    }

    public SolicitudReclamo aprobarSolicitud(Integer id) {
        SolicitudReclamo solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        solicitud.setIdEstado(ESTADO_APROBADO);
        return solicitudRepository.save(solicitud);
    }

    public SolicitudReclamo rechazarSolicitud(Integer id) {
        SolicitudReclamo solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        solicitud.setIdEstado(ESTADO_RECHAZADO);
        return solicitudRepository.save(solicitud);
    }
}
package com.udea.objetos_perdidos_backend.Service;

import com.udea.objetos_perdidos_backend.Dto.NotificacionDTO;
import com.udea.objetos_perdidos_backend.Model.Notificacion;
import com.udea.objetos_perdidos_backend.Model.Persona;
import com.udea.objetos_perdidos_backend.Repository.NotificacionRepository;
import com.udea.objetos_perdidos_backend.Repository.PersonaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionService {

    private static final int TIPO_NOTIFICACION_GENERAL = 1;
    private static final int ROL_ADMIN = 2;

    private final NotificacionRepository notificacionRepository;
    private final PersonaRepository personaRepository;

    public NotificacionService(
            NotificacionRepository notificacionRepository,
            PersonaRepository personaRepository
    ) {
        this.notificacionRepository = notificacionRepository;
        this.personaRepository = personaRepository;
    }

    public List<NotificacionDTO> listarPorCorreo(String correo) {
        return notificacionRepository.listarPorCorreo(correo.toLowerCase().trim())
                .stream()
                .map(n -> new NotificacionDTO(
                        n.getId(),
                        n.getMensaje(),
                        n.getFechaEnvio(),
                        n.getLeida(),
                        n.getTipo()
                ))
                .toList();
    }

    @Transactional
    public Notificacion crearNotificacionParaPersona(
            Integer idPersonaRecibe,
            String mensaje
    ) {
        Notificacion notificacion = new Notificacion();

        notificacion.setIdPersonaRecibe(idPersonaRecibe);
        notificacion.setMensaje(mensaje);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setLeida(false);
        notificacion.setIdTipoNotificacion(TIPO_NOTIFICACION_GENERAL);

        return notificacionRepository.save(notificacion);
    }

    @Transactional
    public Notificacion crearNotificacionPorCorreo(
            String correo,
            String mensaje
    ) {
        Persona persona = personaRepository.findByCorreo(correo.toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        return crearNotificacionParaPersona(persona.getId(), mensaje);
    }

    @Transactional
    public void crearNotificacionParaAdmins(String mensaje) {
        List<Persona> administradores = personaRepository.findByIdRol(ROL_ADMIN);

        for (Persona admin : administradores) {
            crearNotificacionParaPersona(admin.getId(), mensaje);
        }
    }

    @Transactional
    public void crearNotificacionParaAdminsConFallback(
            String mensaje,
            Integer idPersonaFallback
    ) {
        List<Persona> administradores = personaRepository.findByIdRol(ROL_ADMIN);

        if (administradores.isEmpty()) {
            crearNotificacionParaPersona(idPersonaFallback, mensaje);
            return;
        }

        for (Persona admin : administradores) {
            crearNotificacionParaPersona(admin.getId(), mensaje);
        }
    }

    @Transactional
    public Notificacion marcarComoLeida(Integer id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notificacion.setLeida(true);
        return notificacionRepository.save(notificacion);
    }

    @Transactional
    public void borrarNotificacion(Integer id) {
        if (!notificacionRepository.existsById(id)) {
            throw new RuntimeException("Notificación no encontrada");
        }

        notificacionRepository.deleteById(id);
    }

    @Transactional
    public void borrarNotificacionesPorCorreo(String correo) {
        Persona persona = personaRepository.findByCorreo(correo.toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        notificacionRepository.deleteByIdPersonaRecibe(persona.getId());
    }
}
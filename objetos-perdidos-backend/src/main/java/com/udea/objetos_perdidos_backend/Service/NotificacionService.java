package com.udea.objetos_perdidos_backend.Service;

import com.udea.objetos_perdidos_backend.Dto.NotificacionDTO;
import com.udea.objetos_perdidos_backend.Model.Notificacion;
import com.udea.objetos_perdidos_backend.Repository.NotificacionProjection;
import com.udea.objetos_perdidos_backend.Repository.NotificacionRepository;
import com.udea.objetos_perdidos_backend.Repository.PersonaRepository;
import com.udea.objetos_perdidos_backend.Model.Persona;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final PersonaRepository personaRepository;

    public NotificacionService(NotificacionRepository notificacionRepository, PersonaRepository personaRepository) {
        this.notificacionRepository = notificacionRepository;
        this.personaRepository = personaRepository;
    }

    public List<NotificacionDTO> listarPorCorreo(String correo) {
        return notificacionRepository.listarPorCorreo(correo)
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

    public Notificacion marcarComoLeida(Integer id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notificacion.setLeida(true);
        return notificacionRepository.save(notificacion);
    }

    public void borrarNotificacion(Integer id) {
        if (!notificacionRepository.existsById(id)) {
            throw new RuntimeException("Notificación no encontrada");
        }
        notificacionRepository.deleteById(id);
    }

    public void borrarNotificacionesPorCorreo(String correo) {
        Persona persona = personaRepository.findByCorreo(correo.toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        notificacionRepository.deleteByIdPersonaRecibe(persona.getId());
    }
}
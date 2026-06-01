package com.udea.objetos_perdidos_backend.Service;

import com.udea.objetos_perdidos_backend.Dto.SolicitudAdminDTO;
import com.udea.objetos_perdidos_backend.Dto.SolicitudReclamoRequest;
import com.udea.objetos_perdidos_backend.Model.EntregaObjeto;
import com.udea.objetos_perdidos_backend.Model.Objeto;
import com.udea.objetos_perdidos_backend.Model.Persona;
import com.udea.objetos_perdidos_backend.Model.Publicacion;
import com.udea.objetos_perdidos_backend.Model.SolicitudReclamo;
import com.udea.objetos_perdidos_backend.Dto.EntregarSolicitudRequest;
import com.udea.objetos_perdidos_backend.Repository.EntregaObjetoRepository;
import com.udea.objetos_perdidos_backend.Repository.ObjetoRepository;
import com.udea.objetos_perdidos_backend.Repository.PersonaRepository;
import com.udea.objetos_perdidos_backend.Repository.PublicacionRepository;
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
    private static final int ESTADO_ENTREGADO = 2;
    private static final int ESTADO_ANULADO = 13;
    private static final int ESTADO_OBJETO_EN_CUSTODIA = 1;
    private static final int ESTADO_OBJETO_ENTREGADO = 2;
    private static final int ESTADO_PUBLICACION_OCULTA = 12;

    private final SolicitudReclamoRepository solicitudRepository;
    private final PersonaRepository personaRepository;
    private final ObjetoRepository objetoRepository;
    private final PublicacionRepository publicacionRepository;
    private final EntregaObjetoRepository entregaObjetoRepository;

    public SolicitudReclamoService(
            SolicitudReclamoRepository solicitudRepository,
            PersonaRepository personaRepository,
            ObjetoRepository objetoRepository,
            PublicacionRepository publicacionRepository,
            EntregaObjetoRepository entregaObjetoRepository
    ) {
        this.solicitudRepository = solicitudRepository;
        this.personaRepository = personaRepository;
        this.objetoRepository = objetoRepository;
        this.publicacionRepository = publicacionRepository;
        this.entregaObjetoRepository = entregaObjetoRepository;
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
    public List<SolicitudAdminDTO> listarSolicitudesUsuario(String correo) {
        return solicitudRepository.listarSolicitudesUsuario(correo.toLowerCase().trim())
                .stream()
                .map(s -> new SolicitudAdminDTO(
                        s.getId(),
                        s.getDescripcion(),
                        s.getFecha(),
                        s.getFechaAproxPerdida(),
                        s.getIdEstado(),
                        s.getObjeto(),
                        s.getCorreoUsuario(),
                        s.getLugar(),
                        s.getFotografia(),
                        s.getDescripcionObjeto()
                ))
                .toList();
    }
    public SolicitudReclamo aprobarSolicitud(Integer id) {
        SolicitudReclamo solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        // Cambiar solicitud a estado aprobado
        solicitud.setIdEstado(ESTADO_APROBADO);
        solicitudRepository.save(solicitud);

        // Cambiar objeto a estado en custodia
        Objeto objeto = objetoRepository.findById(solicitud.getIdObjeto())
                .orElseThrow(() -> new RuntimeException("Objeto no encontrado"));
        objeto.setIdEstado(ESTADO_OBJETO_EN_CUSTODIA);
        objetoRepository.save(objeto);

        // Ocultar la publicación del objeto
        List<Publicacion> publicaciones = publicacionRepository.findByIdObjeto(solicitud.getIdObjeto());
        for (Publicacion publicacion : publicaciones) {
            publicacion.setIdEstado(ESTADO_PUBLICACION_OCULTA);
            publicacionRepository.save(publicacion);
        }

        return solicitud;
    }

    public SolicitudReclamo rechazarSolicitud(Integer id) {
        SolicitudReclamo solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        solicitud.setIdEstado(ESTADO_RECHAZADO);
        return solicitudRepository.save(solicitud);
    }

    public SolicitudReclamo anularSolicitud(Integer id) {
        SolicitudReclamo solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!solicitud.getIdEstado().equals(ESTADO_PENDIENTE)) {
            throw new RuntimeException("Solo se pueden anular solicitudes pendientes");
        }

        solicitud.setIdEstado(ESTADO_ANULADO);
        return solicitudRepository.save(solicitud);
    }

    public SolicitudReclamo entregarSolicitud(Integer id, EntregarSolicitudRequest request) {
        // Buscar la solicitud
        SolicitudReclamo solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        // Validar que esté aprobada
        if (!solicitud.getIdEstado().equals(ESTADO_APROBADO)) {
            throw new RuntimeException("La solicitud no está aprobada. Estado actual: " + solicitud.getIdEstado());
        }

        // Buscar el objeto
        Objeto objeto = objetoRepository.findById(solicitud.getIdObjeto())
                .orElseThrow(() -> new RuntimeException("Objeto no encontrado"));

        // Crear registro de entrega
        EntregaObjeto entrega = new EntregaObjeto();
        entrega.setIdObjeto(solicitud.getIdObjeto());
        entrega.setIdPersonaRecibe(solicitud.getIdPersona());
        // Buscar administrador que entrega por correo
        Persona admin = personaRepository
                .findByCorreo(request.getCorreoAdmin().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
        entrega.setIdPersonaEntrega(admin.getId());
        entrega.setIdSolicitudReclamo(id);
        String obs = request.getObservaciones();
        if (obs == null || obs.isBlank()) {
            obs = "Entrega registrada desde el sistema";
        }
        entrega.setObservaciones(obs);
        entrega.setFechaEntrega(LocalDateTime.now());
        entregaObjetoRepository.save(entrega);

        // Cambiar objeto a estado entregado
        objeto.setIdEstado(ESTADO_OBJETO_ENTREGADO);
        objetoRepository.save(objeto);

        // Cambiar solicitud a estado entregada
        solicitud.setIdEstado(ESTADO_ENTREGADO);
        solicitudRepository.save(solicitud);

        // Ocultar la publicación asociada al objeto
        List<Publicacion> publicaciones = publicacionRepository.findByIdObjeto(solicitud.getIdObjeto());
        for (Publicacion publicacion : publicaciones) {
            publicacion.setIdEstado(ESTADO_PUBLICACION_OCULTA);
            publicacionRepository.save(publicacion);
        }

        return solicitud;
    }
}
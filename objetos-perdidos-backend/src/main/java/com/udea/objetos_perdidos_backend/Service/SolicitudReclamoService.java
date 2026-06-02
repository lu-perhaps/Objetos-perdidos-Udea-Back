package com.udea.objetos_perdidos_backend.Service;

import com.udea.objetos_perdidos_backend.Dto.EntregarSolicitudRequest;
import com.udea.objetos_perdidos_backend.Dto.SolicitudAdminDTO;
import com.udea.objetos_perdidos_backend.Dto.SolicitudReclamoRequest;
import com.udea.objetos_perdidos_backend.Model.EntregaObjeto;
import com.udea.objetos_perdidos_backend.Model.Objeto;
import com.udea.objetos_perdidos_backend.Model.Persona;
import com.udea.objetos_perdidos_backend.Model.Publicacion;
import com.udea.objetos_perdidos_backend.Model.SolicitudReclamo;
import com.udea.objetos_perdidos_backend.Repository.EntregaObjetoRepository;
import com.udea.objetos_perdidos_backend.Repository.ObjetoRepository;
import com.udea.objetos_perdidos_backend.Repository.PersonaRepository;
import com.udea.objetos_perdidos_backend.Repository.PublicacionRepository;
import com.udea.objetos_perdidos_backend.Repository.SolicitudAdminProjection;
import com.udea.objetos_perdidos_backend.Repository.SolicitudReclamoRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private static final int ESTADO_OBJETO_DISPONIBLE = 3;

    private static final int ESTADO_PUBLICACION_PUBLICADA = 11;
    private static final int ESTADO_PUBLICACION_OCULTA = 12;

    private final SolicitudReclamoRepository solicitudRepository;
    private final PersonaRepository personaRepository;
    private final ObjetoRepository objetoRepository;
    private final PublicacionRepository publicacionRepository;
    private final EntregaObjetoRepository entregaObjetoRepository;
    private final NotificacionService notificacionService;
    private final JdbcTemplate jdbcTemplate;

    public SolicitudReclamoService(
            SolicitudReclamoRepository solicitudRepository,
            PersonaRepository personaRepository,
            ObjetoRepository objetoRepository,
            PublicacionRepository publicacionRepository,
            EntregaObjetoRepository entregaObjetoRepository,
            NotificacionService notificacionService,
            JdbcTemplate jdbcTemplate
    ) {
        this.solicitudRepository = solicitudRepository;
        this.personaRepository = personaRepository;
        this.objetoRepository = objetoRepository;
        this.publicacionRepository = publicacionRepository;
        this.entregaObjetoRepository = entregaObjetoRepository;
        this.notificacionService = notificacionService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public SolicitudReclamo crearSolicitud(SolicitudReclamoRequest request) {
        Persona persona = personaRepository
                .findByCorreo(request.getCorreoUsuario().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("No existe una persona con ese correo"));

        Objeto objeto = objetoRepository.findById(request.getIdObjeto())
                .orElseThrow(() -> new RuntimeException("Objeto no encontrado"));

        SolicitudReclamo solicitud = new SolicitudReclamo();
        solicitud.setDescripcion(request.getDescripcion());
        solicitud.setFecha(LocalDateTime.now());
        solicitud.setIdObjeto(request.getIdObjeto());
        solicitud.setIdPersona(persona.getId());
        solicitud.setIdLugarAproxPerdida(request.getIdLugarAproxPerdida());
        solicitud.setFechaAproxPerdida(request.getFechaAproxPerdida());
        solicitud.setIdEstado(ESTADO_PENDIENTE);

        SolicitudReclamo solicitudGuardada = solicitudRepository.save(solicitud);

        String mensajeAdmin = "Nueva solicitud de reclamo para el objeto \"" +
                objeto.getNombre() +
                "\" registrada por " +
                persona.getCorreo() +
                ".";

        notificacionService.crearNotificacionParaAdminsConFallback(
                mensajeAdmin,
                persona.getId()
        );

        return solicitudGuardada;
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
                        s.getIdReporte(),
                        s.getObjeto(),
                        s.getFotografia(),
                        s.getDescripcionObjeto(),
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
                        s.getIdReporte(),
                        s.getObjeto(),
                        s.getFotografia(),
                        s.getDescripcionObjeto(),
                        s.getCorreoUsuario(),
                        s.getLugar()
                ))
                .toList();
    }

    @Transactional
    public SolicitudReclamo aprobarSolicitud(Integer id) {
        SolicitudReclamo solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!solicitud.getIdEstado().equals(ESTADO_PENDIENTE)) {
            throw new RuntimeException("Solo se pueden aprobar solicitudes pendientes");
        }

        Objeto objeto = objetoRepository.findById(solicitud.getIdObjeto())
                .orElseThrow(() -> new RuntimeException("Objeto no encontrado"));

        solicitud.setIdEstado(ESTADO_APROBADO);
        SolicitudReclamo solicitudGuardada = solicitudRepository.save(solicitud);

        objeto.setIdEstado(ESTADO_OBJETO_EN_CUSTODIA);
        objetoRepository.save(objeto);

        ocultarPublicacionesDelObjeto(solicitud.getIdObjeto());

        String lugarCustodia = obtenerNombreLugarActual(objeto.getIdLugarActual());

        String mensaje = "Tu solicitud de reclamo para el objeto \"" +
                objeto.getNombre() +
                "\" fue aprobada. Dirígete a " +
                lugarCustodia +
                " para continuar con el proceso de entrega.";

        notificacionService.crearNotificacionParaPersona(
                solicitud.getIdPersona(),
                mensaje
        );

        return solicitudGuardada;
    }

    @Transactional
    public SolicitudReclamo rechazarSolicitud(Integer id) {
        SolicitudReclamo solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!solicitud.getIdEstado().equals(ESTADO_PENDIENTE)) {
            throw new RuntimeException("Solo se pueden rechazar solicitudes pendientes");
        }

        Objeto objeto = objetoRepository.findById(solicitud.getIdObjeto())
                .orElseThrow(() -> new RuntimeException("Objeto no encontrado"));

        solicitud.setIdEstado(ESTADO_RECHAZADO);
        SolicitudReclamo solicitudGuardada = solicitudRepository.save(solicitud);

        String mensaje = "Tu solicitud de reclamo para el objeto \"" +
                objeto.getNombre() +
                "\" fue rechazada. Los datos suministrados no coinciden con el objeto registrado.";

        notificacionService.crearNotificacionParaPersona(
                solicitud.getIdPersona(),
                mensaje
        );

        return solicitudGuardada;
    }

    @Transactional
    public SolicitudReclamo anularSolicitud(Integer id) {
        SolicitudReclamo solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!solicitud.getIdEstado().equals(ESTADO_PENDIENTE)) {
            throw new RuntimeException("Solo se pueden anular solicitudes pendientes");
        }

        solicitud.setIdEstado(ESTADO_ANULADO);
        return solicitudRepository.save(solicitud);
    }

    @Transactional
    public SolicitudReclamo cancelarAprobacion(Integer id) {
        SolicitudReclamo solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!solicitud.getIdEstado().equals(ESTADO_APROBADO)) {
            throw new RuntimeException("Solo se puede cancelar una solicitud aprobada");
        }

        Objeto objeto = objetoRepository.findById(solicitud.getIdObjeto())
                .orElseThrow(() -> new RuntimeException("Objeto no encontrado"));

        solicitud.setIdEstado(ESTADO_ANULADO);
        SolicitudReclamo solicitudGuardada = solicitudRepository.save(solicitud);

        objeto.setIdEstado(ESTADO_OBJETO_DISPONIBLE);
        objetoRepository.save(objeto);

        republicarObjeto(solicitud.getIdObjeto());

        String mensaje = "La aprobación de tu solicitud para el objeto \"" +
                objeto.getNombre() +
                "\" fue cancelada porque el proceso de entrega no se completó. " +
                "El objeto volvió a estar disponible en el sistema.";

        notificacionService.crearNotificacionParaPersona(
                solicitud.getIdPersona(),
                mensaje
        );

        return solicitudGuardada;
    }

    @Transactional
    public SolicitudReclamo entregarSolicitud(Integer id, EntregarSolicitudRequest request) {
        SolicitudReclamo solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (!solicitud.getIdEstado().equals(ESTADO_APROBADO)) {
            throw new RuntimeException(
                    "La solicitud no está aprobada. Estado actual: " + solicitud.getIdEstado()
            );
        }

        Objeto objeto = objetoRepository.findById(solicitud.getIdObjeto())
                .orElseThrow(() -> new RuntimeException("Objeto no encontrado"));

        Persona admin = personaRepository
                .findByCorreo(request.getCorreoAdmin().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("Administrador no encontrado"));

        EntregaObjeto entrega = new EntregaObjeto();
        entrega.setIdObjeto(solicitud.getIdObjeto());
        entrega.setIdPersonaRecibe(solicitud.getIdPersona());
        entrega.setIdPersonaEntrega(admin.getId());
        entrega.setIdSolicitudReclamo(id);

        String observaciones = request.getObservaciones();

        if (observaciones == null || observaciones.isBlank()) {
            observaciones = "Entrega registrada desde el sistema";
        }

        entrega.setObservaciones(observaciones);
        entrega.setFechaEntrega(LocalDateTime.now());
        entregaObjetoRepository.save(entrega);

        objeto.setIdEstado(ESTADO_OBJETO_ENTREGADO);
        objetoRepository.save(objeto);

        solicitud.setIdEstado(ESTADO_ENTREGADO);
        SolicitudReclamo solicitudGuardada = solicitudRepository.save(solicitud);

        ocultarPublicacionesDelObjeto(solicitud.getIdObjeto());

        String mensaje = "Tu objeto \"" +
                objeto.getNombre() +
                "\" fue marcado como entregado. El proceso de reclamo ha finalizado.";

        notificacionService.crearNotificacionParaPersona(
                solicitud.getIdPersona(),
                mensaje
        );

        return solicitudGuardada;
    }

    private String obtenerNombreLugarActual(Integer idLugarActual) {
        if (idLugarActual == null) {
            return "la oficina correspondiente";
        }

        try {
            String lugar = jdbcTemplate.queryForObject(
                    "SELECT nombre FROM tbl_lugar WHERE id = ?",
                    String.class,
                    idLugarActual
            );

            if (lugar == null || lugar.isBlank()) {
                return "la oficina correspondiente";
            }

            return lugar;
        } catch (Exception e) {
            return "la oficina correspondiente";
        }
    }

    private void ocultarPublicacionesDelObjeto(Integer idObjeto) {
        List<Publicacion> publicaciones = publicacionRepository.findByIdObjeto(idObjeto);

        for (Publicacion publicacion : publicaciones) {
            publicacion.setIdEstado(ESTADO_PUBLICACION_OCULTA);
            publicacionRepository.save(publicacion);
        }
    }

    private void republicarObjeto(Integer idObjeto) {
        List<Publicacion> publicaciones = publicacionRepository.findByIdObjeto(idObjeto);

        for (Publicacion publicacion : publicaciones) {
            publicacion.setIdEstado(ESTADO_PUBLICACION_PUBLICADA);
            publicacionRepository.save(publicacion);
        }
    }
}
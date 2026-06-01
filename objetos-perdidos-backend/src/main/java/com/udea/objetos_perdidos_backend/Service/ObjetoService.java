package com.udea.objetos_perdidos_backend.Service;

import com.udea.objetos_perdidos_backend.Dto.CrearObjetoRequest;
import com.udea.objetos_perdidos_backend.Dto.ObjetoPublicadoDTO;
import com.udea.objetos_perdidos_backend.Model.Objeto;
import com.udea.objetos_perdidos_backend.Model.Persona;
import com.udea.objetos_perdidos_backend.Model.Publicacion;
import com.udea.objetos_perdidos_backend.Repository.ObjetoPublicadoProjection;
import com.udea.objetos_perdidos_backend.Repository.ObjetoRepository;
import com.udea.objetos_perdidos_backend.Repository.PersonaRepository;
import com.udea.objetos_perdidos_backend.Repository.PublicacionRepository;
import org.springframework.stereotype.Service;
import com.udea.objetos_perdidos_backend.Dto.ActualizarObjetoRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ObjetoService {

    private static final int ESTADO_OBJETO_DISPONIBLE = 3;
    private static final int ESTADO_OBJETO_EN_CUSTODIA = 1;
    private static final int ESTADO_OBJETO_DONADO = 14;
    private static final int ESTADO_OBJETO_DESECHADO = 15;
    private static final int ESTADO_PUBLICACION_PUBLICADO = 11;
    private static final int ESTADO_PUBLICACION_OCULTO = 12;

    private final ObjetoRepository objetoRepository;
    private final PersonaRepository personaRepository;
    private final PublicacionRepository publicacionRepository;

    public ObjetoService(
            ObjetoRepository objetoRepository,
            PersonaRepository personaRepository,
            PublicacionRepository publicacionRepository
    ) {
        this.objetoRepository = objetoRepository;
        this.personaRepository = personaRepository;
        this.publicacionRepository = publicacionRepository;
    }

    public List<ObjetoPublicadoDTO> listarObjetosPublicados() {
        List<ObjetoPublicadoProjection> objetos = objetoRepository.listarObjetosPublicados();

        return objetos.stream()
                .map(objeto -> new ObjetoPublicadoDTO(
                        objeto.getId(),
                        objeto.getNombre(),
                        objeto.getDescripcionGeneral(),
                        objeto.getDescripcionDetallada(),
                        objeto.getFechaHallazgo(),
                        objeto.getFotografia(),
                        objeto.getCategoria(),
                        objeto.getLugarEncontrado(),
                        objeto.getLugarActual()
                ))
                .toList();
    }

    public List<ObjetoPublicadoDTO> listarObjetosAdmin() {
        return objetoRepository.listarObjetosAdmin()
                .stream()
                .map(objeto -> new ObjetoPublicadoDTO(
                        objeto.getId(),
                        objeto.getNombre(),
                        objeto.getDescripcionGeneral(),
                        objeto.getDescripcionDetallada(),
                        objeto.getFechaHallazgo(),
                        objeto.getFotografia(),
                        objeto.getIdEstado(),
                        objeto.getEstado(),
                        objeto.getCategoria(),
                        objeto.getLugarEncontrado(),
                        objeto.getLugarActual()
                ))
                .toList();
    }

    public List<ObjetoPublicadoDTO> listarObjetosVencidos() {
        return objetoRepository.listarObjetosVencidos()
                .stream()
                .map(objeto -> new ObjetoPublicadoDTO(
                        objeto.getId(),
                        objeto.getNombre(),
                        objeto.getDescripcionGeneral(),
                        objeto.getDescripcionDetallada(),
                        objeto.getFechaHallazgo(),
                        objeto.getFotografia(),
                        objeto.getIdEstado(),
                        objeto.getEstado(),
                        objeto.getCategoria(),
                        objeto.getLugarEncontrado(),
                        objeto.getLugarActual(),
                        objeto.getTiempoMaximoAlmacenamiento()
                ))
                .toList();
    }

    public Objeto crearObjetoYPublicar(CrearObjetoRequest request) {
        Persona admin = personaRepository
                .findByCorreo(request.getCorreoAdmin().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("Admin no encontrado"));

        boolean publicar = Boolean.TRUE.equals(request.getPublicar());

        Objeto objeto = new Objeto();
        objeto.setNombre(request.getNombre());
        objeto.setDescripcionGeneral(request.getDescripcionGeneral());
        objeto.setDescripcionDetallada(request.getDescripcionDetallada());
        objeto.setIdCategoria(request.getIdCategoria());
        objeto.setFechaHallazgo(request.getFechaHallazgo());
        objeto.setFotografia(request.getFotografia());
        objeto.setIdLugarEncontrado(request.getIdLugarEncontrado());
        objeto.setIdLugarActual(request.getIdLugarActual());

        if (publicar) {
            objeto.setIdEstado(ESTADO_OBJETO_DISPONIBLE);
        } else {
            objeto.setIdEstado(ESTADO_OBJETO_EN_CUSTODIA);
        }

        Objeto objetoGuardado = objetoRepository.save(objeto);

        if (publicar) {
            Publicacion publicacion = new Publicacion();
            publicacion.setIdObjeto(objetoGuardado.getId());
            publicacion.setFecha(LocalDateTime.now());
            publicacion.setIdPersonaPublica(admin.getId());
            publicacion.setIdEstado(ESTADO_PUBLICACION_PUBLICADO);

            publicacionRepository.save(publicacion);
        }

        return objetoGuardado;
    }
    
    public ObjetoPublicadoDTO obtenerObjetoPorId(Integer id) {
        ObjetoPublicadoProjection objeto = objetoRepository.obtenerObjetoPorId(id);
        if (objeto == null) {
            throw new RuntimeException("Objeto no encontrado");
        }

        return new ObjetoPublicadoDTO(
                objeto.getId(),
                objeto.getNombre(),
                objeto.getDescripcionGeneral(),
                objeto.getDescripcionDetallada(),
                objeto.getFechaHallazgo(),
                objeto.getFotografia(),
                objeto.getIdEstado(),
                objeto.getEstado(),
                objeto.getCategoria(),
                objeto.getLugarEncontrado(),
                objeto.getLugarActual()
        );
    }
    public Objeto actualizarObjeto(Integer id, ActualizarObjetoRequest request) {
        Objeto objeto = objetoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Objeto no encontrado"));

        objeto.setNombre(request.getNombre());
        objeto.setDescripcionGeneral(request.getDescripcionGeneral());
        objeto.setDescripcionDetallada(request.getDescripcionDetallada());
        objeto.setIdCategoria(request.getIdCategoria());
        objeto.setFechaHallazgo(request.getFechaHallazgo());
        objeto.setFotografia(request.getFotografia());
        objeto.setIdLugarEncontrado(request.getIdLugarEncontrado());
        objeto.setIdLugarActual(request.getIdLugarActual());

        return objetoRepository.save(objeto);
    }   

    public Objeto registrarDisposicionFinal(Integer idObjeto, Integer nuevoEstado) {
        if (!nuevoEstado.equals(ESTADO_OBJETO_DONADO)
                && !nuevoEstado.equals(ESTADO_OBJETO_DESECHADO)) {
            throw new RuntimeException("Estado de disposición final no válido");
        }

        Objeto objeto = objetoRepository.findById(idObjeto)
                .orElseThrow(() -> new RuntimeException("Objeto no encontrado"));

        objeto.setIdEstado(nuevoEstado);

        List<Publicacion> publicaciones = publicacionRepository.findByIdObjeto(idObjeto);
        for (Publicacion publicacion : publicaciones) {
            publicacion.setIdEstado(ESTADO_PUBLICACION_OCULTO);
            publicacionRepository.save(publicacion);
        }

        return objetoRepository.save(objeto);
    }

    public void ocultarObjeto(Integer id) {
        Objeto objeto = objetoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Objeto no encontrado"));

        List<Publicacion> publicaciones = publicacionRepository.findByIdObjeto(id);

        for (Publicacion publicacion : publicaciones) {
            publicacion.setIdEstado(ESTADO_PUBLICACION_OCULTO);
            publicacionRepository.save(publicacion);
        }
    }
    public Objeto actualizarFotografia(Integer idObjeto, String url) {
        Objeto objeto = objetoRepository.findById(idObjeto)
                .orElseThrow(() -> new RuntimeException("Objeto no encontrado"));

        objeto.setFotografia(url);
        return objetoRepository.save(objeto);
    }
}
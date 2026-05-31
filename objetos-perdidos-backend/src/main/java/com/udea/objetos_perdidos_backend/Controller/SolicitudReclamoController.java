package com.udea.objetos_perdidos_backend.Controller;

import com.udea.objetos_perdidos_backend.Dto.SolicitudAdminDTO;
import com.udea.objetos_perdidos_backend.Dto.SolicitudReclamoRequest;
import com.udea.objetos_perdidos_backend.Model.SolicitudReclamo;
import com.udea.objetos_perdidos_backend.Dto.EntregarSolicitudRequest;
import com.udea.objetos_perdidos_backend.Service.SolicitudReclamoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin(origins = "*")
public class SolicitudReclamoController {

    private final SolicitudReclamoService solicitudService;

    public SolicitudReclamoController(SolicitudReclamoService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    public SolicitudReclamo crearSolicitud(@RequestBody SolicitudReclamoRequest request) {
        return solicitudService.crearSolicitud(request);
    }

    @GetMapping("/admin")
    public List<SolicitudAdminDTO> listarSolicitudesAdmin() {
        return solicitudService.listarSolicitudesAdmin();
    }

    @PutMapping("/{id}/aprobar")
    public SolicitudReclamo aprobarSolicitud(@PathVariable Integer id) {
        return solicitudService.aprobarSolicitud(id);
    }

    @PutMapping("/{id}/rechazar")
    public SolicitudReclamo rechazarSolicitud(@PathVariable Integer id) {
        return solicitudService.rechazarSolicitud(id);
    }

    @PutMapping("/{id}/entregar")
    public SolicitudReclamo entregarSolicitud(@PathVariable Integer id, @RequestBody EntregarSolicitudRequest request) {
        return solicitudService.entregarSolicitud(id, request);
    }
}
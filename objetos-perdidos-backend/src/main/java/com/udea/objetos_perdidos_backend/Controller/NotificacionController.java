package com.udea.objetos_perdidos_backend.Controller;

import com.udea.objetos_perdidos_backend.Dto.NotificacionDTO;
import com.udea.objetos_perdidos_backend.Model.Notificacion;
import com.udea.objetos_perdidos_backend.Service.NotificacionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping("/{correo}")
    public List<NotificacionDTO> listarPorCorreo(@PathVariable String correo) {
        return notificacionService.listarPorCorreo(correo);
    }

    @PutMapping("/{id}/leer")
    public Notificacion marcarComoLeida(@PathVariable Integer id) {
        return notificacionService.marcarComoLeida(id);
    }

    @DeleteMapping("/{id}")
    public void borrarNotificacion(@PathVariable Integer id) {
        notificacionService.borrarNotificacion(id);
    }

    @DeleteMapping("/persona/{correo}")
    public void borrarNotificacionesPorCorreo(@PathVariable String correo) {
        notificacionService.borrarNotificacionesPorCorreo(correo);
    }
}
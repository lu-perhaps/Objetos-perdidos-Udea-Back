package com.udea.objetos_perdidos_backend.Controller;

import com.udea.objetos_perdidos_backend.Dto.CrearObjetoRequest;
import com.udea.objetos_perdidos_backend.Dto.ObjetoPublicadoDTO;
import com.udea.objetos_perdidos_backend.Model.Objeto;
import com.udea.objetos_perdidos_backend.Service.ObjetoService;
import org.springframework.web.bind.annotation.*;
import com.udea.objetos_perdidos_backend.Dto.ActualizarObjetoRequest;
import java.util.List;

@RestController
@RequestMapping("/api/objetos")
@CrossOrigin(origins = "*")
public class ObjetoController {

    private final ObjetoService objetoService;

    public ObjetoController(ObjetoService objetoService) {
        this.objetoService = objetoService;
    }

    @GetMapping
    public List<ObjetoPublicadoDTO> listarObjetosPublicados() {
        return objetoService.listarObjetosPublicados();
    }

    @GetMapping("/admin")
    public List<ObjetoPublicadoDTO> listarObjetosAdmin() {
        return objetoService.listarObjetosAdmin();
    }
        @GetMapping("/vencidos")
    public List<ObjetoPublicadoDTO> listarObjetosVencidos() {
        return objetoService.listarObjetosVencidos();
    }

    @PutMapping("/{id}/donar")
    public Objeto donarObjeto(@PathVariable Integer id) {
        return objetoService.registrarDisposicionFinal(id, 14);
    }

    @PutMapping("/{id}/desechar")
    public Objeto desecharObjeto(@PathVariable Integer id) {
        return objetoService.registrarDisposicionFinal(id, 15);
    }
    @GetMapping("/{id}")
    public ObjetoPublicadoDTO obtenerObjetoPorId(@PathVariable Integer id) {
        return objetoService.obtenerObjetoPorId(id);
    }

    @PostMapping
    public Objeto crearObjeto(@RequestBody CrearObjetoRequest request) {
        return objetoService.crearObjetoYPublicar(request);
    }
    @PutMapping("/{id}")
    public Objeto actualizarObjeto(
            @PathVariable Integer id,
            @RequestBody ActualizarObjetoRequest request
    ) {
        return objetoService.actualizarObjeto(id, request);
    }

    @DeleteMapping("/{id}")
    public void ocultarObjeto(@PathVariable Integer id) {
        objetoService.ocultarObjeto(id);
    }
}
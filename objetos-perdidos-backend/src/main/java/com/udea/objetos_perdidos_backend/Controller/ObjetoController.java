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
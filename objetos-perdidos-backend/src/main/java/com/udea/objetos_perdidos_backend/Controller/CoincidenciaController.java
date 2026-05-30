package com.udea.objetos_perdidos_backend.Controller;

import com.udea.objetos_perdidos_backend.Dto.CoincidenciaRequest;
import com.udea.objetos_perdidos_backend.Service.CoincidenciaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coincidencias")
@CrossOrigin(origins = "*")
public class CoincidenciaController {

    private final CoincidenciaService coincidenciaService;

    public CoincidenciaController(CoincidenciaService coincidenciaService) {
        this.coincidenciaService = coincidenciaService;
    }

    @PostMapping
    public void procesarCoincidencia(@RequestBody CoincidenciaRequest request) {
        coincidenciaService.procesarCoincidencia(request);
    }
}
package com.udea.objetos_perdidos_backend.Controller;

import com.udea.objetos_perdidos_backend.Model.Lugar;
import com.udea.objetos_perdidos_backend.Service.LugarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lugares")
@CrossOrigin(origins = "*")
public class LugarController {

    private final LugarService lugarService;

    public LugarController(LugarService lugarService) {
        this.lugarService = lugarService;
    }

    @GetMapping
    public List<Lugar> listarLugares() {
        return lugarService.listarLugares();
    }
}
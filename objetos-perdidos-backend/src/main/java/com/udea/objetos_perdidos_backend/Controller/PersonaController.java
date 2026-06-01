package com.udea.objetos_perdidos_backend.Controller;

import com.udea.objetos_perdidos_backend.Dto.PersonaDTO;
import com.udea.objetos_perdidos_backend.Service.PersonaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@CrossOrigin(origins = "*")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping("/estudiantes")
    public List<PersonaDTO> listarEstudiantes() {
        return personaService.listarEstudiantes();
    }
}
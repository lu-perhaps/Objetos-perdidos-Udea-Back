package com.udea.objetos_perdidos_backend.Service;

import com.udea.objetos_perdidos_backend.Dto.PersonaDTO;
import com.udea.objetos_perdidos_backend.Repository.PersonaProjection;
import com.udea.objetos_perdidos_backend.Repository.PersonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public List<PersonaDTO> listarEstudiantes() {
        List<PersonaProjection> estudiantes = personaRepository.listarEstudiantes();

        return estudiantes.stream()
                .map(p -> new PersonaDTO(
                        p.getId(),
                        p.getNombre(),
                        p.getCorreo(),
                        p.getCelular(),
                        p.getNumDocumento(),
                        p.getIdRol(),
                        p.getIdEstado(),
                        p.getIdTipoDocumento(),
                        p.getTipoDocumento()
                ))
                .toList();
    }
}
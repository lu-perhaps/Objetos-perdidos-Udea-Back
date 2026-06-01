package com.udea.objetos_perdidos_backend.Security;

import com.udea.objetos_perdidos_backend.Dto.AuthMeDTO;
import com.udea.objetos_perdidos_backend.Dto.ActualizarPerfilRequest;
import com.udea.objetos_perdidos_backend.Model.Persona;
import com.udea.objetos_perdidos_backend.Repository.PersonaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthService {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String UDEA_DOMAIN = "@udea.edu.co";
    private static final int ROL_USUARIO = 1;
    private static final int ESTADO_ACTIVO = 4;

    private final PersonaRepository personaRepository;
    private final JwtDecoder jwtDecoder;
    private final HttpServletRequest request;

    public AuthService(PersonaRepository personaRepository,
                       JwtDecoder jwtDecoder,
                       HttpServletRequest request) {
        this.personaRepository = personaRepository;
        this.jwtDecoder = jwtDecoder;
        this.request = request;
    }

    public AuthMeDTO obtenerUsuarioAutenticado() {
        Persona persona = getPersonaFromToken();
        return mapToDto(persona);
    }

    public AuthMeDTO actualizarPerfil(ActualizarPerfilRequest perfilRequest) {
        Persona persona = getPersonaFromToken();

        persona.setNombre(perfilRequest.getNombre());
        persona.setCelular(perfilRequest.getCelular());
        persona.setNumDocumento(perfilRequest.getNumDocumento());
        persona.setIdTipoDocumento(perfilRequest.getIdTipoDocumento());

        personaRepository.save(persona);
        return mapToDto(persona);
    }

    private Persona getPersonaFromToken() {
        String token = getBearerToken();
        Jwt jwt = decodeJwt(token);

        String correo = jwt.getClaimAsString("email");
        if (correo == null || correo.isBlank()) {
            throw new RuntimeException("Token inválido: no se encontró el correo");
        }

        String correoNormalizado = correo.toLowerCase(Locale.ROOT).trim();

        if (!correoNormalizado.endsWith(UDEA_DOMAIN)) {
            throw new RuntimeException("Correo inválido o dominio no autorizado");
        }

        String nombre = jwt.getClaimAsString("name");

        return personaRepository.findByCorreo(correoNormalizado)
                .orElseGet(() -> crearPersonaInicial(correoNormalizado, nombre));
    }

    private String getBearerToken() {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            throw new RuntimeException("Authorization header inválido");
        }
        return authorization.substring(BEARER_PREFIX.length()).trim();
    }

    private Jwt decodeJwt(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (JwtException ex) {
            throw new RuntimeException("Token inválido o expirado", ex);
        }
    }

    private Persona crearPersonaInicial(String correo, String nombre) {
        Persona persona = new Persona();
        persona.setCorreo(correo);
        persona.setNombre(nombre);
        persona.setIdRol(ROL_USUARIO);
        persona.setIdEstado(ESTADO_ACTIVO);
        return personaRepository.save(persona);
    }

    private AuthMeDTO mapToDto(Persona persona) {
        boolean perfilCompleto = persona.getNombre() != null && !persona.getNombre().isBlank()
                && persona.getCelular() != null && !persona.getCelular().isBlank()
                && persona.getNumDocumento() != null && !persona.getNumDocumento().isBlank()
                && persona.getIdTipoDocumento() != null;

        return new AuthMeDTO(
                persona.getId(),
                persona.getNombre(),
                persona.getCorreo(),
                persona.getIdRol(),
                persona.getIdEstado(),
                perfilCompleto
        );
    }
}
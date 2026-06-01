package com.udea.objetos_perdidos_backend.Controller;

import com.udea.objetos_perdidos_backend.Dto.ActualizarPerfilRequest;
import com.udea.objetos_perdidos_backend.Dto.AuthMeDTO;
import com.udea.objetos_perdidos_backend.Security.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    public AuthMeDTO obtenerUsuarioAutenticado() {
        return authService.obtenerUsuarioAutenticado();
    }

    @PutMapping("/me/perfil")
    public AuthMeDTO actualizarPerfil(@RequestBody ActualizarPerfilRequest request) {
        return authService.actualizarPerfil(request);
    }
}
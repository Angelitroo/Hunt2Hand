package com.hunt2hand.controller;

import com.hunt2hand.dto.*;
import com.hunt2hand.model.Usuario;
import com.hunt2hand.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public Usuario registro(@RequestBody RegistroDTO registroDTO){
        return usuarioService.registrarUsuario(registroDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaDTO> registro(@RequestBody LoginDTO dto){
        return usuarioService.login(dto);
    }

    @PutMapping("/activar")
    public ResponseEntity<PerfilDTO> activarCuenta(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        try {
            PerfilDTO perfilDTO = usuarioService.activarCuenta(token);
            return ResponseEntity.ok(perfilDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/recuperar-contrasena")
    public ResponseEntity<?> recuperarContrasena(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        usuarioService.recuperarContrasena(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/restablecer-contrasena")
    public ResponseEntity<?> restablecerContrasena(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        usuarioService.restablecerContrasena(token, newPassword);
        return ResponseEntity.ok().build();
    }
}

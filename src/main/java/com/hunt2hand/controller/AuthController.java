package com.hunt2hand.controller;

import com.hunt2hand.dto.LoginDTO;
import com.hunt2hand.dto.RegistroDTO;
import com.hunt2hand.dto.RespuestaDTO;
import com.hunt2hand.model.Usuario;
import com.hunt2hand.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private UsuarioService service;

    @PostMapping("/registro")
    public Usuario registro(@RequestBody RegistroDTO registroDTO){
        return service.registrarUsuario(registroDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaDTO> registro(@RequestBody LoginDTO dto){
        return service.login(dto);
    }

    @PostMapping("/recuperar-contrasena")
    public ResponseEntity<?> recuperarContrasena(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        service.recuperarContrasena(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/restablecer-contrasena")
    public ResponseEntity<?> restablecerContrasena(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        service.restablecerContrasena(token, newPassword);
        return ResponseEntity.ok().build();
    }
}

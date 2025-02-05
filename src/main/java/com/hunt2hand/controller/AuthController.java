package com.hunt2hand.controller;

import com.hunt2hand.dto.LoginDTO;
import com.hunt2hand.dto.RegistroDTO;
import com.hunt2hand.dto.RespuestaDTO;
import com.hunt2hand.model.Usuario;
import com.hunt2hand.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/actualizar/{id}")
    public Usuario actualizarUsuario(@PathVariable Long id, @RequestBody RegistroDTO registroDTO) {return service.actualizarUsuario(id, registroDTO);}
}

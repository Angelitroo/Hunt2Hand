package com.hunt2hand.controller;

import com.hunt2hand.model.Usuario;
import com.hunt2hand.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping("/{id}")
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioService.obtenerUsuarioPorId(id);
    }
}

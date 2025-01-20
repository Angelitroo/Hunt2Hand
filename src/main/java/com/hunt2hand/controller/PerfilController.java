package com.hunt2hand.controller;

import com.hunt2hand.model.Perfil;
import com.hunt2hand.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfiles")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;

    // Obtener todos los perfiles
    @GetMapping
    public ResponseEntity<List<Perfil>> obtenerTodosLosPerfiles() {
        return ResponseEntity.ok(perfilService.obtenerTodosLosPerfiles());
    }

    // Buscar un perfil por username
    @GetMapping("/buscar/{username}")
    public ResponseEntity<Perfil> buscarPorUsername(@PathVariable String username) {
        return perfilService.buscarPorUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un perfil por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPerfil(@PathVariable Long id) {
        perfilService.eliminarPerfil(id);
        return ResponseEntity.noContent().build();
    }

    // Modificar un perfil existente
    @PutMapping("/{id}")
    public ResponseEntity<Perfil> modificarPerfil(@PathVariable Long id, @RequestBody Perfil perfilModificado) {
        try {
            Perfil perfilActualizado = perfilService.modificarPerfil(id, perfilModificado);
            return ResponseEntity.ok(perfilActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
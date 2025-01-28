package com.hunt2hand.controller;

import com.hunt2hand.dto.PerfilDTO;
import com.hunt2hand.dto.ProductoDTO;
import com.hunt2hand.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perfiles")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;

    @GetMapping("/")
    public List<PerfilDTO> getAll() {
        return perfilService.getAll();
    }

    @GetMapping("/{id}")
    public PerfilDTO getById(@PathVariable Long id) {
        return perfilService.getById(id);
    }

    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<PerfilDTO> getByNombre(@PathVariable String nombre) {
        PerfilDTO perfilDTO = perfilService.getByNombre(nombre);

        if (perfilDTO == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(perfilDTO);
    }

    @PostMapping("/guardar/{idUsuario}")
    public PerfilDTO guardar(@RequestBody PerfilDTO perfilDTO, @PathVariable Long idUsuario) {
        return perfilService.guardar(perfilDTO, idUsuario);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        try {
            String resultado = perfilService.eliminar(id);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
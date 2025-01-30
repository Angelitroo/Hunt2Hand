package com.hunt2hand.controller;

import com.hunt2hand.dto.PerfilDTO;
import com.hunt2hand.dto.ProductoDTO;
import com.hunt2hand.dto.SeguirDTO;
import com.hunt2hand.model.Seguidores;
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

    @GetMapping({"/buscar/{nombre}"})
    public ResponseEntity<List<PerfilDTO>> buscarPorNombre(@PathVariable String nombre) {
        List<PerfilDTO> perfiles = perfilService.getByNombre(nombre);

        return ResponseEntity.ok(perfiles);
    }

    @PostMapping("/guardar/{idUsuario}")
    public PerfilDTO guardar(@RequestBody PerfilDTO perfilDTO, @PathVariable Long idUsuario) {
        return perfilService.guardar(perfilDTO, idUsuario);
    }

    @PutMapping("/actualizar/{id}")
    public PerfilDTO actualizar(@RequestBody PerfilDTO perfilDTO, @PathVariable Long idPerfil) {
        return perfilService.actualizar(perfilDTO, idPerfil);
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

    @PostMapping("/seguir")
    public ResponseEntity<Seguidores> seguirPerfil(@RequestBody SeguirDTO seguirDTO) {
        try {
            Seguidores seguidores = perfilService.seguirPerfil(seguirDTO);
            return ResponseEntity.ok(seguidores);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/seguidores/{id}")
    public ResponseEntity<List<PerfilDTO>> obtenerSeguidores(@PathVariable Long id) {
        try {
            List<PerfilDTO> seguidores = perfilService.obtenerSeguidores(id);
            return ResponseEntity.ok(seguidores);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/seguidos/{id}")
    public ResponseEntity<List<PerfilDTO>> obtenerSeguidos(@PathVariable Long id) {
        try {
            List<PerfilDTO> seguidos = perfilService.obtenerSeguidos(id);
            return ResponseEntity.ok(seguidos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
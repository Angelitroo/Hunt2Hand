package com.hunt2hand.controller;

import com.hunt2hand.dto.ReseñaDTO;
import com.hunt2hand.service.ReseñaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reseña")
public class ReseñaController {

    @Autowired
    private ReseñaService reseñaService;

    @PostMapping("/crear")
    public ResponseEntity<ReseñaDTO> crearReseña(@RequestBody ReseñaDTO reseñaDTO, @PathVariable Long id_perfilvalorado, @PathVariable Long id_perfilvalorador) {
        ReseñaDTO reseñaCreada = reseñaService.guardarReseña(reseñaDTO, id_perfilvalorado, id_perfilvalorador);
        return ResponseEntity.ok(reseñaCreada);
    }
}
package com.hunt2hand.controller;

import com.hunt2hand.dto.ReseñaDTO;
import com.hunt2hand.model.Reseña;
import com.hunt2hand.service.ReseñaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reseña")
public class ReseñaController {

    @Autowired
    private ReseñaService reseñaService;


    @PostMapping("/crear/{idPerfilValorador}/{idPerfilValorado}")
    public ResponseEntity<Reseña> crearReseña(@PathVariable Long idPerfilValorador,
                                              @PathVariable Long idPerfilValorado,
                                              @RequestBody ReseñaDTO reseñaDTO) {
        Reseña nuevaReseña = reseñaService.crearReseña(idPerfilValorador, idPerfilValorado, reseñaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReseña);
    }

}

package com.hunt2hand.controller;

import com.hunt2hand.dto.ReseñaDTO;
import com.hunt2hand.service.ReseñaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reseña")
public class ReseñaController {

    @Autowired
    private ReseñaService reseñaService;


    @PostMapping("/crear/{id_perfilvalorado}/{id_perfilvalorador}")
    public ReseñaDTO crearReseña(
            @RequestBody ReseñaDTO reseñaDTO,
            @PathVariable Long id_perfilvalorado,
            @PathVariable Long id_perfilvalorador) {
        System.out.println("Llamada al endpoint con id_perfilvalorado: " + id_perfilvalorado +
                " y id_perfilvalorador: " + id_perfilvalorador);
        return reseñaService.crearReseña(reseñaDTO, id_perfilvalorado, id_perfilvalorador);
    }

}

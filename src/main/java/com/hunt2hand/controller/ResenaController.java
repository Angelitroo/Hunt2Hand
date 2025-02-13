package com.hunt2hand.controller;

import com.hunt2hand.dto.ResenaDTO;
import com.hunt2hand.model.Resena;
import com.hunt2hand.service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resena")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;


    @PostMapping("/crear/{idPerfilValorador}/{idPerfilValorado}")
    public ResponseEntity<Resena> crearResena(@PathVariable Long idPerfilValorador, @PathVariable Long idPerfilValorado, @RequestBody  ResenaDTO resenaDTO) {
        Resena nuevaResena = resenaService.crearResena(idPerfilValorador, idPerfilValorado, resenaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaResena);
    }


    @GetMapping("/buscar/media/{idPerfilValorado}")
    public ResponseEntity<Double> buscarResenaMedia(@PathVariable Long idPerfilValorado) {
        Double mediaValoracion = resenaService.buscarResenaMedia(idPerfilValorado);
        return ResponseEntity.ok(mediaValoracion);
    }


    @GetMapping("/buscar/{idPerfilValorador}/{idPerfilValorado}")
    public ResponseEntity<ResenaDTO> buscarResena(@PathVariable Long idPerfilValorador, @PathVariable Long idPerfilValorado) {
        ResenaDTO resena = resenaService.buscarResena(idPerfilValorador, idPerfilValorado);
        return ResponseEntity.ok(resena);
    }
}

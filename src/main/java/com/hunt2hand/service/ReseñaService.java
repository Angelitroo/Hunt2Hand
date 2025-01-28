package com.hunt2hand.service;

import com.hunt2hand.dto.PerfilDTO;
import com.hunt2hand.dto.ReseñaDTO;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Reseña;
import com.hunt2hand.repository.PerfilRepository;
import com.hunt2hand.repository.ReseñaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReseñaService {

    private final ReseñaRepository reseñaRepository;
    private final PerfilRepository perfilRepository;

    public ReseñaService(ReseñaRepository reseñaRepository, PerfilService perfilService, PerfilRepository perfilRepository) {
        this.reseñaRepository = reseñaRepository;
        this.perfilRepository = perfilRepository;
    }

    public ReseñaDTO crearReseña(ReseñaDTO reseñaDTO, Long id_perfilvalorado, Long id_perfilvalorador) {
        PerfilDTO perfilValorado = perfilRepository.findById(id_perfilvalorado).orElseThrow()-> new RuntimeException("Perfil no encontrado con ID: " + id_perfilvalorado));
        PerfilDTO perfilValorador = perfilRepository.findById(id_perfilvalorador);

        Reseña reseña =
    }

}
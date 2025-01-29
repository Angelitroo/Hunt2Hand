package com.hunt2hand.service;

import com.hunt2hand.dto.ReseñaDTO;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Producto;
import com.hunt2hand.model.Reseña;
import com.hunt2hand.repository.PerfilRepository;
import com.hunt2hand.repository.ReseñaRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public ReseñaDTO crearReseña(ReseñaDTO reseñaDTO, Long id_perfilvalorado, Long id_perfilvalorador) {
        Perfil perfilValorado = perfilRepository.findById(id_perfilvalorado)
                .orElseThrow(() -> new RuntimeException("Perfil valorado con id " + id_perfilvalorado + " no encontrado"));

        Perfil perfilValorador = perfilRepository.findById(id_perfilvalorador)
                .orElseThrow(() -> new RuntimeException("Perfil valorador con id " + id_perfilvalorador + " no encontrado"));

        Reseña reseña = new Reseña();
        reseña.setId(reseñaDTO.getId());
        reseña.setValoracion(reseñaDTO.getValoracion());
        reseña.setPerfilValorado(perfilValorado);
        reseña.setPerfilValorador(perfilValorador);

        Reseña reseñaGuardada = reseñaRepository.save(reseña);


        ReseñaDTO dto = new ReseñaDTO();
        dto.setId(reseñaGuardada.getId());
        dto.setValoracion(reseñaGuardada.getValoracion());
        dto.setId_perfilvalorado(reseñaGuardada.getPerfilValorado().getId());
        dto.setId_perfilvalorador(reseñaGuardada.getPerfilValorador().getId());

        return dto;
    }
}
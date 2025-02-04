package com.hunt2hand.service;

import com.hunt2hand.dto.PerfilDTO;
import com.hunt2hand.dto.ProductoDTO;
import com.hunt2hand.dto.ReseñaDTO;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Producto;
import com.hunt2hand.model.Reseña;
import com.hunt2hand.repository.PerfilRepository;
import com.hunt2hand.repository.ReseñaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ReseñaService {

    @Autowired
    private ReseñaRepository reseñaRepository;

    @Autowired
    private PerfilRepository perfilRepository;



    public Reseña crearReseña(Long idPerfilValorador, Long idPerfilValorado, ReseñaDTO reseñaDTO) {
        Perfil perfilValorador = perfilRepository.findById(idPerfilValorador)
                .orElseThrow(() -> new RuntimeException("Perfil valorador no encontrado"));

        Perfil perfilValorado = perfilRepository.findById(idPerfilValorado)
                .orElseThrow(() -> new RuntimeException("Perfil valorado no encontrado"));

        if (reseñaDTO.getValoracion() < 1 || reseñaDTO.getValoracion() > 5) {
            throw new IllegalArgumentException("La valoración debe estar entre 1 y 5.");
        }

        Reseña reseña = new Reseña();
        reseña.setValoracion(reseñaDTO.getValoracion());
        reseña.setPerfilValorador(perfilValorador);
        reseña.setPerfilValorado(perfilValorado);

        return reseñaRepository.save(reseña);

    }
}

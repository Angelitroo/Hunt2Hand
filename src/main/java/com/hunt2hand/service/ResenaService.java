package com.hunt2hand.service;

import com.hunt2hand.dto.ResenaDTO;
import com.hunt2hand.exception.RecursoNoEncontrado;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Resena;
import com.hunt2hand.repository.PerfilRepository;
import com.hunt2hand.repository.ResenaRepository;
import jdk.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@AllArgsConstructor
public class ResenaService {
    private ResenaRepository resenaRepository;
    private PerfilRepository perfilRepository;

    public Resena crearResena(Long idPerfilValorador, Long idPerfilValorado, ResenaDTO resenaDTO) {
        Perfil perfilValorador = perfilRepository.findById(idPerfilValorador)
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil valorador no encontrado"));

        Perfil perfilValorado = perfilRepository.findById(idPerfilValorado)
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil valorado no encontrado"));

        if (resenaDTO.getValoracion() < 1 || resenaDTO.getValoracion() > 5) {
            throw new IllegalArgumentException("La valoración debe estar entre 1 y 5.");
        }

        Resena resena = new Resena();
        resena.setValoracion(resenaDTO.getValoracion());
        resena.setPerfilValorador(perfilValorador);
        resena.setPerfilValorado(perfilValorado);

        return resenaRepository.save(resena);
    }

    public void eliminarResenaByPerfil(Long id) {
        List<Resena> resenas = resenaRepository.findByPerfilValorador_IdOrPerfilValorado_Id(id, id);
        resenaRepository.deleteAll(resenas);
    }
}
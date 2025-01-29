package com.hunt2hand.service;

import com.hunt2hand.dto.PerfilDTO;
import com.hunt2hand.dto.SeguirDTO;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Seguidores;
import com.hunt2hand.model.Usuario;
import com.hunt2hand.repository.PerfilRepository;
import com.hunt2hand.repository.SeguidoresRepository;
import com.hunt2hand.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Validated
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final SeguidoresRepository seguidoresRepository;
    public List<PerfilDTO> getAll() {
        List<Perfil> perfiles = perfilRepository.findAll();

        if (perfiles == null) {
            return Collections.emptyList();
        }

        return perfiles.stream()
                .map(this::convertirAPerfilDTO)
                .collect(Collectors.toList());
    }

    public PerfilDTO getById(Long id) {
        Perfil perfil = perfilRepository.findById(id).orElse(null);

        if (perfil == null) {
            return null;
        }

        return convertirAPerfilDTO(perfil);
    }

    public PerfilDTO getByNombre(String nombre) {
        Perfil perfil = perfilRepository.findByNombre(nombre).orElse(null);

        if (perfil == null) {
            return null;
        }

        return convertirAPerfilDTO(perfil);
    }

    public PerfilDTO guardar(PerfilDTO perfilDTO, Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Viaje con id " + idUsuario + " no encontrado"));

        Perfil perfil = new Perfil();
        perfil.setId(perfilDTO.getId());
        perfil.setNombre(perfilDTO.getNombre());
        perfil.setApellido(perfilDTO.getApellido());
        perfil.setUbicacion(perfilDTO.getUbicacion());
        perfil.setImagen(perfilDTO.getImagen());
        perfil.setBaneado(perfilDTO.getBaneado());
        perfil.setUsuario(usuario);

        Perfil perfilGuardado = perfilRepository.save(perfil);

        return convertirAPerfilDTO(perfilGuardado);
    }

    public String eliminar(Long id) {
        if (!perfilRepository.existsById(id)) {
            throw new IllegalArgumentException("El id no existe");
        }
        perfilRepository.deleteById(id);
        return "Eliminado correctamente";
    }

    public Seguidores seguirPerfil(SeguirDTO seguirDTO) {
        Perfil seguidor = perfilRepository.findById(seguirDTO.getIdSeguidor()).orElse(null);
        Perfil seguido = perfilRepository.findById(seguirDTO.getIdSeguido()).orElse(null);

        if (seguidor == null || seguido == null) {
            throw new IllegalArgumentException("Perfil no encontrado");
        }

        Seguidores seguidores = new Seguidores();
        seguidores.setSeguidor(seguidor);
        seguidores.setSeguido(seguido);

        return seguidoresRepository.save(seguidores);
    }

    public List<PerfilDTO> obtenerSeguidores(Long idPerfil) {
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new IllegalArgumentException("Perfil no encontrado"));
        return seguidoresRepository.findBySeguido(perfil).stream()
                .map(Seguidores::getSeguidor)
                .map(this::convertirAPerfilDTO)
                .collect(Collectors.toList());
    }

    public List<PerfilDTO> obtenerSeguidos(Long idPerfil) {
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new IllegalArgumentException("Perfil no encontrado"));
        return seguidoresRepository.findBySeguidor(perfil).stream()
                .map(Seguidores::getSeguido)
                .map(this::convertirAPerfilDTO)
                .collect(Collectors.toList());
    }

    public PerfilDTO convertirAPerfilDTO(Perfil perfil) {
        if (perfil == null) {
            return null;
        }

        PerfilDTO dto = new PerfilDTO();
        dto.setId(perfil.getId());
        dto.setNombre(perfil.getNombre());
        dto.setApellido(perfil.getApellido());
        dto.setUbicacion(perfil.getUbicacion());
        dto.setImagen(perfil.getImagen());
        dto.setBaneado(perfil.isBaneado());
        dto.setUsuario(perfil.getUsuario().getId());
        return dto;
    }


}
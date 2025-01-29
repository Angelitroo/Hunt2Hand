package com.hunt2hand.service;

import com.hunt2hand.dto.PerfilDTO;
import com.hunt2hand.dto.ProductoDTO;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Producto;
import com.hunt2hand.model.Usuario;
import com.hunt2hand.repository.PerfilRepository;
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

    public List<PerfilDTO> getAll() {
        List<Perfil> perfiles = perfilRepository.findAll();

        if (perfiles == null) {
            return Collections.emptyList();
        }

        return perfiles.stream()
                .map(perfil -> {
                    PerfilDTO dto = new PerfilDTO();
                    dto.setId(perfil.getId());
                    dto.setNombre(perfil.getNombre());
                    dto.setApellido(perfil.getApellido());
                    dto.setUbicacion(perfil.getUbicacion());
                    dto.setImagen(perfil.getImagen());
                    dto.setBaneado(perfil.isBaneado());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public PerfilDTO getById(Long id) {
        Perfil perfil = perfilRepository.findById(id).orElse(null);

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


    public List<PerfilDTO> getByNombre(String nombre) {
        String patron = nombre + "%";

        List<Perfil> perfiles = perfilRepository.findByNombreLikeIgnoreCase(patron);

        if (perfiles == null) {
            return Collections.emptyList();
        }

        return perfiles.stream().map(perfil -> {
            PerfilDTO dto = new PerfilDTO();
            dto.setId(perfil.getId());
            dto.setNombre(perfil.getNombre());
            dto.setApellido(perfil.getApellido());
            dto.setUbicacion(perfil.getUbicacion());
            dto.setImagen(perfil.getImagen());
            dto.setBaneado(perfil.isBaneado());
            return dto;
        }).collect(Collectors.toList());
    }

    public PerfilDTO guardar(PerfilDTO perfilDTO, Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario con id " + idUsuario + " no encontrado"));;

        Perfil perfil = new Perfil();
        perfil.setId(perfilDTO.getId());
        perfil.setNombre(perfilDTO.getNombre());
        perfil.setApellido(perfilDTO.getApellido());
        perfil.setUbicacion(perfilDTO.getUbicacion());
        perfil.setImagen(perfilDTO.getImagen());
        perfil.setBaneado(perfilDTO.getBaneado());
        perfil.setUsuario(usuario);

        Perfil perfilGuardado = perfilRepository.save(perfil);

        PerfilDTO dto = new PerfilDTO();
        dto.setId(perfilGuardado.getId());
        dto.setNombre(perfilGuardado.getNombre());
        dto.setApellido(perfilGuardado.getApellido());
        dto.setUbicacion(perfilGuardado.getUbicacion());
        dto.setImagen(perfilGuardado.getImagen());
        dto.setBaneado(perfilGuardado.isBaneado());
        dto.setUsuario(perfilGuardado.getUsuario().getId());

        return dto;
    }

    public PerfilDTO actualizar(PerfilDTO perfilDTO, Long idPerfil) {
        Perfil perfil = perfilRepository.findById(idPerfil).orElseThrow(() -> new IllegalArgumentException("El id no existe"));

        perfil.setNombre(perfilDTO.getNombre());
        perfil.setApellido(perfilDTO.getApellido());
        perfil.setUbicacion(perfilDTO.getUbicacion());
        perfil.setImagen(perfilDTO.getImagen());
        perfil.setBaneado(perfilDTO.getBaneado());

        Perfil perfilActualizado = perfilRepository.save(perfil);

        PerfilDTO dto = new PerfilDTO();
        dto.setId(perfilActualizado.getId());
        dto.setNombre(perfilActualizado.getNombre());
        dto.setApellido(perfilActualizado.getApellido());
        dto.setUbicacion(perfilActualizado.getUbicacion());
        dto.setImagen(perfilActualizado.getImagen());
        dto.setBaneado(perfilActualizado.isBaneado());

        return dto;
    }

    public String eliminar(Long id) {
        if (!perfilRepository.existsById(id)) {
            throw new IllegalArgumentException("El id no existe");
        }
        perfilRepository.deleteById(id);
        return "Eliminado correctamente";
    }

    public Perfil buscarPorUsuario(Usuario usuario){
        return perfilRepository.findTopByUsuario(usuario);
    }
}
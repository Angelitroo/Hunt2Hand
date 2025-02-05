package com.hunt2hand.service;

import com.hunt2hand.dto.PerfilActualizarDTO;
import com.hunt2hand.dto.PerfilDTO;
import com.hunt2hand.dto.SeguirDTO;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Seguidores;
import com.hunt2hand.model.Usuario;
import com.hunt2hand.repository.PerfilRepository;
import com.hunt2hand.repository.SeguidoresRepository;
import com.hunt2hand.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;


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



    public PerfilActualizarDTO actualizar(PerfilActualizarDTO perfilActualizarDTO, Long idPerfil) {
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new IllegalArgumentException("El id no existe"));

        perfil.setNombre(perfilActualizarDTO.getNombre());
        perfil.setApellido(perfilActualizarDTO.getApellido());
        perfil.setUbicacion(perfilActualizarDTO.getUbicacion());
        perfil.setImagen(perfilActualizarDTO.getImagen());

        if (perfilActualizarDTO.getUsername() != null && !perfilActualizarDTO.getUsername().isEmpty()) {
            perfil.getUsuario().setUsername(perfilActualizarDTO.getUsername());
        }

        if (perfilActualizarDTO.getPassword() != null && !perfilActualizarDTO.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(perfilActualizarDTO.getPassword());
            perfil.getUsuario().setPassword(encodedPassword);
            perfilActualizarDTO.setPassword(encodedPassword);
        }

        Perfil perfilActualizado = perfilRepository.save(perfil);

        PerfilActualizarDTO dto = new PerfilActualizarDTO();
        dto.setId(perfilActualizado.getId());
        dto.setNombre(perfilActualizado.getNombre());
        dto.setApellido(perfilActualizado.getApellido());
        dto.setUbicacion(perfilActualizado.getUbicacion());
        dto.setImagen(perfilActualizado.getImagen());
        dto.setUsername(perfilActualizado.getUsuario().getUsername());
        dto.setPassword(perfilActualizarDTO.getPassword());

        return dto;
    }




    public PerfilActualizarDTO getActualizadoById(Long id) {
        Perfil perfil = perfilRepository.findById(id).orElse(null);

        if (perfil == null) {
            return null;
        }

        PerfilActualizarDTO dto = new PerfilActualizarDTO();
        dto.setId(perfil.getId());
        dto.setNombre(perfil.getNombre());
        dto.setApellido(perfil.getApellido());
        dto.setUbicacion(perfil.getUbicacion());
        dto.setImagen(perfil.getImagen());
        dto.setUsername(perfil.getUsuario().getUsername());
        dto.setPassword(perfil.getUsuario().getPassword());
        return dto;
    }

}
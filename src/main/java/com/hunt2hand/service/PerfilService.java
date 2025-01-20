package com.hunt2hand.service;

import com.hunt2hand.dto.PerfilCrearDTO;
import com.hunt2hand.dto.PerfilDTO;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.repository.PerfilRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Validated
public class PerfilService {

    private final PerfilRepository perfilRepository;

    /**
     * Este método extrae todos los perfiles de base de datos
     *
     * @return
     */
    public List<PerfilDTO> getAll() {
        List<Perfil> perfiles = perfilRepository.findAll();
        List<PerfilDTO> perfilDTOS = new ArrayList<>();

        for (Perfil p : perfiles) {
            PerfilDTO dto = new PerfilDTO();
            dto.setNombre(p.getNombre());
            dto.setApellido(p.getApellido());
            dto.setUbicacion(p.getUbicacion());
            dto.setImagen(p.getImagen());
            dto.setBaneado(p.isBaneado());
            perfilDTOS.add(dto);
        }

        return perfilDTOS;
    }

    /**
     * Busca perfiles por coincidencia en nombre, apellidos o mail
     *
     * @param busqueda
     * @return
     */
    public List<PerfilDTO> buscar(String busqueda) {
        List<Perfil> perfiles = perfilRepository.buscar(busqueda);
        List<PerfilDTO> perfilDTOS = new ArrayList<>();

        for (Perfil p : perfiles) {
            PerfilDTO dto = new PerfilDTO();
            dto.setNombre(p.getNombre());
            dto.setApellido(p.getApellido());
            dto.setUbicacion(p.getUbicacion());
            dto.setImagen(p.getImagen());
            dto.setBaneado(p.isBaneado());
            perfilDTOS.add(dto);
        }

        return perfilDTOS;
    }

    /**
     * Este método busca un perfil a partir de su id
     *
     * @param id
     * @return
     */
    public Perfil getById(Integer id) {
        return perfilRepository.findById(id).orElse(null);
    }

    /**
     * Este método guarda un perfil nuevo o modifica uno existente
     *
     * @param dto
     * @return
     */
    public Perfil guardar(@Valid PerfilCrearDTO dto) throws Exception {
        Perfil perfilGuardar = new Perfil();
        perfilGuardar.setNombre(dto.getNombre());
        perfilGuardar.setApellido(dto.getApellido());
        perfilGuardar.setUbicacion(dto.getUbicacion());
        perfilGuardar.setImagen(dto.getImagen());

        return perfilRepository.save(perfilGuardar);
    }

    /**
     * Elimina un perfil a través de su id
     *
     * @param id
     */
    public String eliminar(Integer id) {
        String mensaje;
        Perfil perfil = getById(id);

        if (perfil == null) {
            return "El perfil con el id indicado no existe";
        }

        try {
            perfilRepository.deleteById(id);
            perfil = getById(id);
            if (perfil != null) {
                mensaje = "No se ha podido eliminar el perfil";
            } else {
                mensaje = "Perfil eliminado correctamente";
            }
        } catch (Exception e) {
            mensaje = "No se ha podido eliminar el perfil";
        }

        return mensaje;
    }

    public void eliminar(Perfil perfil) {
        perfilRepository.delete(perfil);
    }

    public Perfil guardarPerfil(Perfil perfil) {
        return perfilRepository.save(perfil);
    }

    // Obtener todos los perfiles
    public List<Perfil> obtenerTodosLosPerfiles() {
        return perfilRepository.findAll();
    }

    // Buscar un perfil por username
    public Optional<Perfil> buscarPorUsername(String username) {
        return perfilRepository.findByUsuarioUsername(username);
    }

    // Eliminar un perfil por su ID
    public void eliminarPerfil(Long id) {
        perfilRepository.deleteById(Math.toIntExact(id));
    }

    // Modificar un perfil existente
    public Perfil modificarPerfil(Long id, Perfil perfilModificado) {
        return perfilRepository.findById(Math.toIntExact(id)).map(perfil -> {
            perfil.setNombre(perfilModificado.getNombre());
            perfil.setApellido(perfilModificado.getApellido());
            perfil.setUbicacion(perfilModificado.getUbicacion());
            perfil.setImagen(perfilModificado.getImagen());
            perfil.setBaneado(perfilModificado.isBaneado());
            return perfilRepository.save(perfil);
        }).orElseThrow(() -> new IllegalArgumentException("Perfil no encontrado con ID: " + id));
    }


}
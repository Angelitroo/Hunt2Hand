package com.hunt2hand.service;

import com.hunt2hand.dto.*;
import com.hunt2hand.exception.RecursoNoEncontrado;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Seguidores;
import com.hunt2hand.model.Usuario;
import com.hunt2hand.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@AllArgsConstructor
public class PerfilService {
    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final SeguidoresRepository seguidoresRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final MensajeService mensajeService;
    private final FavoritosService favoritosService;
    private final ChatService chatService;
    private final ReporteService reporteService;
    private final ResenaService resenaService;
    private final ProductoService productoService;

    public List<PerfilDTO> getAll() {
        List<Perfil> perfiles = perfilRepository.findAll();

        if (perfiles.isEmpty()) {
            throw new RecursoNoEncontrado("No se encontraron perfiles");
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
                    dto.setUsuario(perfil.getUsuario().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public PerfilDTO getById(Long id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil con id " + id + " no encontrado"));

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

        if (perfiles == null || perfiles.isEmpty()) {
            throw new RecursoNoEncontrado("No se encontraron perfiles con el nombre: " + nombre);
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
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario con id " + idUsuario + " no encontrado"));

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

    @Transactional
    public String eliminar(Long id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil con id " + id + " no encontrado"));

        Usuario usuario = perfil.getUsuario();

        if (!perfilRepository.existsById(id)) {
            throw new RecursoNoEncontrado("Perfil con id " + id + " no encontrado");
        }

        favoritosService.eliminarFavoritosByPerfil(id);
        mensajeService.eliminarMensajesByPerfil(id);
        chatService.eliminarChatByPerfil(id);
        reporteService.eliminarReportesByPerfil(id);
        resenaService.eliminarResenaByPerfil(id);
        eliminarSeguidoresByPerfil(id);
        productoService.eliminarProductoByPerfil(id);
        perfilRepository.delete(perfil);
        usuarioRepository.delete(usuario);

        return "Eliminado correctamente";
    }

    @Transactional
    public String eliminarPorBan(Long id) {
        if (!perfilRepository.existsById(id)) {
            throw new RecursoNoEncontrado("Perfil con id " + id + " no encontrado");
        }

        favoritosService.eliminarFavoritosByPerfil(id);
        mensajeService.eliminarMensajesByPerfil(id);
        chatService.eliminarChatByPerfil(id);
        resenaService.eliminarResenaByPerfil(id);
        eliminarSeguidoresByPerfil(id);
        productoService.eliminarProductoByPerfil(id);

        return "Eliminado correctamente";
    }

    public PerfilDTO banear(BanearPerfilDTO banearPerfilDTO) {
        Perfil perfil = perfilRepository.findById(banearPerfilDTO.getIdPerfil())
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil con id " + banearPerfilDTO.getIdPerfil() + " no encontrado"));

        perfil.setBaneado(true);
        perfilRepository.save(perfil);

        eliminarPorBan(banearPerfilDTO.getIdPerfil());
        enviarEmailBaneado(perfil.getUsuario().getEmail(), perfil.getUsuario().getUsername(), banearPerfilDTO.getMotivo());

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

    public PerfilDTO desbanear(DesbanearPerfilDTO desbanearPerfilDTO) {
        Perfil perfil = perfilRepository.findById(desbanearPerfilDTO.getIdPerfil())
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil con id " + desbanearPerfilDTO.getIdPerfil() + " no encontrado"));

        perfil.setBaneado(false);
        perfilRepository.save(perfil);

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
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil con id " + idPerfil + " no encontrado"));

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
        dto.setEmail(perfilActualizado.getUsuario().getEmail());
        dto.setUsername(perfilActualizado.getUsuario().getUsername());
        dto.setPassword(perfilActualizarDTO.getPassword());

        return dto;
    }

    public void enviarEmailBaneado(String email, String username, String motivo) {
        String emailContent = "<html>" +
                "<body style=\"padding: 20px; font-family: Arial, sans-serif;\">" +
                "<div style=\"max-width: 600px; margin: auto; background: #e6a1f1; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">" +
                "<h1 style=\"color: #333;\">Cuenta Baneada en Hunt2Hand</h1>" +
                "<p>Hola " + username + ",</p>" +
                "<p>Lamentamos informarte que tu cuenta ha sido baneada debido a la siguiente razón:</p>" +
                "<p>" + motivo + "</p>" +
                "<p>Si crees que esto es un error o tienes alguna pregunta, por favor contacta con nuestro equipo de soporte.</p>" +
                "<p>Gracias.</p>" +
                "<p>El equipo de Hunt2Hand</p>" +
                "<hr>" +
                "<p><small>Visita nuestra <a href=\"http://localhost:4200/ayuda\">página de ayuda</a> para más información.</small></p>" +
                "</div>" +
                "</body>" +
                "</html>";

        emailService.sendEmail(email, "Cuenta Baneada en Hunt2Hand", emailContent);
    }


    public Seguidores seguirPerfil(SeguirDTO seguirDTO) {
        Perfil seguidor = perfilRepository.findById(seguirDTO.getIdSeguidor())
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil seguidor no encontrado"));
        Perfil seguido = perfilRepository.findById(seguirDTO.getIdSeguido())
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil seguido no encontrado"));

        Seguidores seguidores = new Seguidores();
        seguidores.setSeguidor(seguidor);
        seguidores.setSeguido(seguido);

        return seguidoresRepository.save(seguidores);
    }

    public void dejarDeSeguirPerfil(SeguirDTO seguirDTO) {
        Perfil seguidor = perfilRepository.findById(seguirDTO.getIdSeguidor())
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil seguidor no encontrado"));
        Perfil seguido = perfilRepository.findById(seguirDTO.getIdSeguido())
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil seguido no encontrado"));

        Seguidores seguidores = seguidoresRepository.findBySeguidorAndSeguido(seguidor, seguido)
                .orElseThrow(() -> new RecursoNoEncontrado("Relación de seguimiento no encontrada"));

        seguidoresRepository.delete(seguidores);
    }

    public List<PerfilDTO> obtenerSeguidores(Long idPerfil) {
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil con id " + idPerfil + " no encontrado"));
        return seguidoresRepository.findBySeguido(perfil).stream()
                .map(Seguidores::getSeguidor)
                .map(this::convertirAPerfilDTO)
                .collect(Collectors.toList());
    }

    public List<PerfilDTO> obtenerSeguidos(Long idPerfil) {
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil con id " + idPerfil + " no encontrado"));
        return seguidoresRepository.findBySeguidor(perfil).stream()
                .map(Seguidores::getSeguido)
                .map(this::convertirAPerfilDTO)
                .collect(Collectors.toList());
    }

    public void eliminarSeguidoresByPerfil(Long id) {
        List<Seguidores> seguidores = seguidoresRepository.findBySeguidor_IdOrSeguido_Id(id, id);
        seguidoresRepository.deleteAll(seguidores);
    }

    public boolean esSeguidor(Long idSeguidor, Long idSeguido) {
        Perfil seguidor = perfilRepository.findById(idSeguidor)
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil seguidor no encontrado"));
        Perfil seguido = perfilRepository.findById(idSeguido)
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil seguido no encontrado"));

        return seguidoresRepository.existsBySeguidorAndSeguido(seguidor, seguido);
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



    public PerfilActualizarDTO getActualizadoById(Long id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Perfil con id " + id + " no encontrado"));

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
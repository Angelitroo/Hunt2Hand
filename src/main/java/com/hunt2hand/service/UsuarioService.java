package com.hunt2hand.service;

import com.hunt2hand.dto.*;
import com.hunt2hand.exception.RecursoNoEncontrado;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Usuario;
import com.hunt2hand.repository.PerfilRepository;
import com.hunt2hand.repository.UsuarioRepository;
import com.hunt2hand.security.JWTService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Validated
@AllArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilService perfilService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final EmailService emailService;

    private final Map<String, String> resetTokens = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> tokenExpiryDates = new ConcurrentHashMap<>();
    private final PerfilRepository perfilRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findTopByUsername(username).orElseThrow(() ->
                new RecursoNoEncontrado("Usuario no encontrado con el nombre: " + username));
    }

    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontrado("Usuario no encontrado con el id: " + id));

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setUsername(usuario.getUsername());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setRol(usuario.getRol().name());

        return usuarioDTO;
    }

    public Usuario registrarUsuario(RegistroDTO dto) {
        if (usuarioRepository.findTopByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario '" + dto.getUsername() + "' ya está en uso.");
        }
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail(dto.getEmail());
        nuevoUsuario.setUsername(dto.getUsername());
        nuevoUsuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        nuevoUsuario.setRol(dto.getRol());

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        PerfilDTO perfilDTO = new PerfilDTO();
        perfilDTO.setNombre(dto.getNombre());
        perfilDTO.setApellido(dto.getApellido());
        perfilDTO.setUbicacion(dto.getUbicacion());
        perfilDTO.setImagen(dto.getImagen());
        perfilDTO.setBaneado(false);
        perfilDTO.setActivado(false);
        perfilDTO.setUsuario(usuarioGuardado.getId());

        perfilService.guardar(perfilDTO, usuarioGuardado.getId());

        enviarEmailBienvenida(usuarioGuardado.getEmail(), usuarioGuardado.getUsername());

        return usuarioGuardado;
    }


    public PerfilDTO activarCuenta(String token) {
        String username = resetTokens.get(token);
        LocalDateTime expiryDate = tokenExpiryDates.get(token);

        if (username != null && expiryDate != null && expiryDate.isAfter(LocalDateTime.now())) {
            Usuario usuario = usuarioRepository.findTopByUsername(username)
                    .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado con el nombre: " + username));
            Perfil perfil = perfilRepository.findByUsuarioId(usuario.getId());

            perfil.setActivado(true);
            perfilRepository.save(perfil);

            resetTokens.remove(token);
            tokenExpiryDates.remove(token);

            PerfilDTO dto = new PerfilDTO();
            dto.setId(perfil.getId());
            dto.setNombre(perfil.getNombre());
            dto.setApellido(perfil.getApellido());
            dto.setUbicacion(perfil.getUbicacion());
            dto.setImagen(perfil.getImagen());
            dto.setActivado(perfil.isActivado());
            dto.setUsuario(perfil.getUsuario().getId());

            return dto;
        } else {
            throw new IllegalArgumentException("Token no válido o ha expirado");
        }
    }


    public ResponseEntity<RespuestaDTO> login(LoginDTO dto) {
        Optional<Usuario> usuarioOpcional = usuarioRepository.findTopByUsername(dto.getUsername());

        if (usuarioOpcional.isPresent()) {
            Usuario usuario = usuarioOpcional.get();
            Perfil perfil = perfilRepository.findByUsuarioId(usuario.getId());

            if (!perfil.isActivado()) {
                throw new IllegalArgumentException("La cuenta no está activada. Por favor, revisa tu correo electrónico para activar tu cuenta.");
            }

            if (passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
                String token = jwtService.generateToken(usuario);
                return ResponseEntity
                        .ok(RespuestaDTO
                                .builder()
                                .estado(HttpStatus.OK.value())
                                .token(token).build());
            } else {
                throw new BadCredentialsException("Contraseña incorrecta");
            }
        } else {
            throw new RecursoNoEncontrado("Usuario no encontrado");
        }
    }

    public void enviarEmailBienvenida(String email, String username) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findTopByUsername(username);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            String token = jwtService.generateToken(usuario);

            String activationLink = "https://hunt2hand.onrender.com/activar-cuenta?token=" + token;
            String emailContent = "<html>" +
                    "<body style=\"padding: 20px; font-family: Arial, sans-serif;\">" +
                    "<div style=\"max-width: 600px; margin: auto; background: #e6a1f1; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">" +
                    "<h1 style=\"color: #333;\">Bienvenido a Hunt2Hand</h1>" +
                    "<p>Hola " + username + ",</p>" +
                    "<p>Gracias por registrarte en nuestra aplicación. Por favor, haz clic en el enlace de abajo para activar tu cuenta:</p>" +
                    "<a href=\"" + activationLink + "\" style=\"display: inline-block; padding: 15px 30px; font-size: 18px; color: #fff; background-color: #007bff; text-decoration: none; border-radius: 5px;\">Activar Cuenta</a>" +
                    "<p>Si tienes alguna pregunta o necesitas ayuda, no dudes en contactarnos.</p>" +
                    "<p>Gracias.</p>" +
                    "<p>El equipo de Hunt2Hand</p>" +
                    "<hr>" +
                    "<p><small>Visita nuestra <a href=\"https://hunt2hand.onrender.com/ayuda\">página de ayuda</a> para más información.</small></p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            emailService.sendEmail(email, "Bienvenido a Hunt2Hand", emailContent);
        } else {
            throw new RecursoNoEncontrado("Usuario no encontrado con el nombre: " + username);
        }
    }

    public void recuperarContrasena(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            String token = UUID.randomUUID().toString();
            resetTokens.put(token, usuario.getUsername());
            tokenExpiryDates.put(token, LocalDateTime.now().plusHours(1));

            String resetLink = "https://hunt2hand.onrender.com/restablecer-contrasena?token=" + token;
            String emailContent = "<html>" +
                    "<body style=\"padding: 20px; font-family: Arial, sans-serif;\">" +
                    "<div style=\"max-width: 600px; margin: auto; background: #e6a1f1; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">" +
                    "<h1 style=\"color: #333;\">Recuperación de Contraseña</h1>" +
                    "<p>Hola " + usuario.getUsername() + ",</p>" +
                    "<p>Hemos recibido una solicitud para restablecer tu contraseña. Haz clic en el botón de abajo para restablecerla:</p>" +
                    "<a href=\"" + resetLink + "\" style=\"display: inline-block; padding: 15px 30px; font-size: 18px; color: #fff; background-color: #007bff; text-decoration: none; border-radius: 5px;\">Restablecer Contraseña</a>" +
                    "<p>Si no solicitaste un cambio de contraseña, puedes ignorar este correo.</p>" +
                    "<p>Gracias.</p>" +
                    "<p>El equipo de Hunt2Hand</p>" +
                    "<hr>" +
                    "<p><small>Al hacer clic en el botón, aceptas nuestros <a href=\"https://hunt2hand.onrender.com/terminos\">Términos de Servicio</a> y <a href=\"http://localhost:4200/privacidad\">Política de Privacidad</a>.</small></p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            emailService.sendEmail(email, "Recuperación de Contraseña", emailContent);
        } else {
            throw new RecursoNoEncontrado("Usuario no encontrado con el correo: " + email);
        }
    }

    public void restablecerContrasena(String token, String newPassword) {
        String username = resetTokens.get(token);
        LocalDateTime expiryDate = tokenExpiryDates.get(token);

        if (username != null && expiryDate != null && expiryDate.isAfter(LocalDateTime.now())) {
            Optional<Usuario> usuarioOptional = usuarioRepository.findTopByUsername(username);
            if (usuarioOptional.isPresent()) {
                Usuario usuario = usuarioOptional.get();
                usuario.setPassword(passwordEncoder.encode(newPassword));
                usuarioRepository.save(usuario);

                resetTokens.remove(token);
                tokenExpiryDates.remove(token);
            } else {
                throw new RecursoNoEncontrado("Usuario no encontrado");
            }
        } else {
            throw new IllegalArgumentException("Token no válido o ha expirado");
        }
    }
}
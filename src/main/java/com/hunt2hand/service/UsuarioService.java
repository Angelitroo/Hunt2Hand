package com.hunt2hand.service;

import com.hunt2hand.dto.LoginDTO;
import com.hunt2hand.dto.RegistroDTO;
import com.hunt2hand.dto.RespuestaDTO;
import com.hunt2hand.dto.PerfilDTO;
import com.hunt2hand.exception.RecursoNoEncontrado;
import com.hunt2hand.model.Usuario;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findTopByUsername(username).orElseThrow(() ->
                new RecursoNoEncontrado("Usuario no encontrado con el nombre: " + username));
    }

    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontrado("Usuario no encontrado con el id: " + id));
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

        PerfilDTO perfilDTO = new PerfilDTO();
        perfilDTO.setNombre(dto.getNombre());
        perfilDTO.setApellido(dto.getApellido());
        perfilDTO.setUbicacion(dto.getUbicacion());
        perfilDTO.setImagen(dto.getImagen());
        perfilDTO.setBaneado(dto.isBaneado());

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        perfilDTO.setUsuario(usuarioGuardado.getId());
        perfilService.guardar(perfilDTO, usuarioGuardado.getId());

        enviarEmailBienvenida(nuevoUsuario.getEmail(), nuevoUsuario.getUsername());

        return usuarioGuardado;
    }

    public ResponseEntity<RespuestaDTO> login(LoginDTO dto) {
        Optional<Usuario> usuarioOpcional = usuarioRepository.findTopByUsername(dto.getUsername());

        if (usuarioOpcional.isPresent()) {
            Usuario usuario = usuarioOpcional.get();

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
        String emailContent = "<html>" +
                "<body style=\"padding: 20px; font-family: Arial, sans-serif;\">" +
                "<div style=\"max-width: 600px; margin: auto; background: #e6a1f1; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">" +
                "<h1 style=\"color: #333;\">Bienvenido a Hunt2Hand</h1>" +
                "<p>Hola " + username + ",</p>" +
                "<p>Gracias por registrarte en nuestra aplicación. Estamos emocionados de tenerte a bordo.</p>" +
                "<p>Si tienes alguna pregunta o necesitas ayuda, no dudes en contactarnos.</p>" +
                "<p>Gracias.</p>" +
                "<p>El equipo de Hunt2Hand</p>" +
                "<hr>" +
                "<p><small>Visita nuestra <a href=\"http://localhost:4200/ayuda\">página de ayuda</a> para más información.</small></p>" +
                "</div>" +
                "</body>" +
                "</html>";

        emailService.sendEmail(email, "Bienvenido a Hunt2Hand", emailContent);
    }

    public void recuperarContrasena(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            String token = UUID.randomUUID().toString();
            resetTokens.put(token, usuario.getUsername());
            tokenExpiryDates.put(token, LocalDateTime.now().plusHours(1));

            String resetLink = "http://localhost:4200/restablecer-contrasena?token=" + token;
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
                    "<p><small>Al hacer clic en el botón, aceptas nuestros <a href=\"http://localhost:4200/terminos\">Términos de Servicio</a> y <a href=\"http://localhost:4200/privacidad\">Política de Privacidad</a>.</small></p>" +
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
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
        perfilDTO.setImagen(dto.getUsername());
        perfilDTO.setBaneado(dto.isBaneado());

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        perfilDTO.setUsuario(usuarioGuardado.getId());
        perfilService.guardar(perfilDTO, usuarioGuardado.getId());

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

    public void recuperarContrasena(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            String token = UUID.randomUUID().toString();
            resetTokens.put(token, usuario.getUsername());
            tokenExpiryDates.put(token, LocalDateTime.now().plusHours(1));

            String resetLink = "http://localhost:4200/restablecer-contrasena?token=" + token;
            emailService.sendEmail(email, "Recuperación de Contraseña", "Para restablecer su contraseña, haga clic en el siguiente enlace: " + resetLink);
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
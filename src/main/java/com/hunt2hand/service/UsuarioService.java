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

import java.util.Optional;

@Service
@Validated
@AllArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilService perfilService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

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
}
package com.hunt2hand.service;


import com.hunt2hand.dto.LoginDTO;
import com.hunt2hand.dto.RegistroDTO;
import com.hunt2hand.dto.RespuestaDTO;
import com.hunt2hand.dto.PerfilDTO;
import com.hunt2hand.model.Perfil;
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

import java.util.Optional;
@Service
@AllArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilService perfilService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findTopByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Usuario no encontrado con el nombre: " + username));
    }

    public Usuario registrarUsuario(RegistroDTO dto) {
        // Verificar si el username ya existe
        if (usuarioRepository.findTopByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario '" + dto.getUsername() + "' ya está en uso.");
        }

        // Crear el usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(dto.getUsername());
        nuevoUsuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        nuevoUsuario.setRol(dto.getRol());

        // Crear el perfil asociado
        PerfilDTO perfilDTO = new PerfilDTO();
        perfilDTO.setNombre(dto.getNombre());
        perfilDTO.setApellido(dto.getApellido());
        perfilDTO.setUbicacion(dto.getUbicacion());
        perfilDTO.setImagen(dto.getUsername());
        perfilDTO.setBaneado(dto.isBaneado());

        // Guardar usuario y perfil
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        perfilDTO.setUsuario(usuarioGuardado.getId());
        perfilService.guardar(perfilDTO, usuarioGuardado.getId());

        return usuarioGuardado;
    }

    public Usuario actualizarUsuario(Long idUsuario, RegistroDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario con id " + idUsuario + " no encontrado"));

        usuarioExistente.setUsername(dto.getUsername());
        usuarioExistente.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuarioExistente.setRol(dto.getRol());

        Perfil perfilExistente = perfilService.buscarPorUsuario(usuarioExistente);
        if (perfilExistente != null) {
            perfilExistente.setNombre(dto.getNombre());
            perfilExistente.setApellido(dto.getApellido());
            perfilExistente.setUbicacion(dto.getUbicacion());
            perfilExistente.setImagen(dto.getUsername());
            perfilExistente.setBaneado(dto.isBaneado());
            perfilService.actualizar(perfilService.convertirAPerfilDTO(perfilExistente), perfilExistente.getId());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public ResponseEntity<RespuestaDTO> login(LoginDTO dto) {
        // Buscar usuario por nombre de usuario
        Optional<Usuario> usuarioOpcional = usuarioRepository.findTopByUsername(dto.getUsername());

        if (usuarioOpcional.isPresent()) {
            Usuario usuario = usuarioOpcional.get();

            // Verificar la contraseña
            if (passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {

                // Contraseña válida, devolver token de acceso
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
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}

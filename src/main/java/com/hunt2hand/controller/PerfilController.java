package com.hunt2hand.controller;

import com.hunt2hand.dto.PerfilActualizarDTO;
import com.hunt2hand.dto.PerfilDTO;
import com.hunt2hand.dto.SeguirDTO;
import com.hunt2hand.model.Favoritos;
import com.hunt2hand.model.Seguidores;
import com.hunt2hand.service.FavoritosService;
import com.hunt2hand.service.PerfilService;
import com.hunt2hand.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/perfiles")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private FavoritosService favoritosService;

    @GetMapping("/")
    public List<PerfilDTO> getAll() {
        return perfilService.getAll();
    }

    @GetMapping("/{id}")
    public PerfilDTO getById(@PathVariable Long id) {
        return perfilService.getById(id);
    }

    @GetMapping({"/buscar/{nombre}"})
    public ResponseEntity<List<PerfilDTO>> buscarPorNombre(@PathVariable String nombre) {
        List<PerfilDTO> perfiles = perfilService.getByNombre(nombre);

        return ResponseEntity.ok(perfiles);
    }

    @PostMapping("/guardar/{idUsuario}")
    public PerfilDTO guardar(@RequestBody PerfilDTO perfilDTO, @PathVariable Long idUsuario) {
        return perfilService.guardar(perfilDTO, idUsuario);
    }

    @GetMapping("actualizado/{id}")
    public PerfilActualizarDTO getActualizadoById(@PathVariable Long id) {
        return perfilService.getActualizadoById(id);
    }

    @PutMapping("/actualizar/{id}")
    public PerfilActualizarDTO actualizar(@RequestBody PerfilActualizarDTO perfilActualizarDTO, @PathVariable Long id) {
        return perfilService.actualizar(perfilActualizarDTO, id);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        try {
            String resultado = perfilService.eliminar(id);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/seguir")
    public ResponseEntity<Seguidores> seguirPerfil(@RequestBody SeguirDTO seguirDTO) {
        try {
            Seguidores seguidores = perfilService.seguirPerfil(seguirDTO);
            return ResponseEntity.ok(seguidores);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/dejar-seguir")
    public ResponseEntity<String> dejarDeSeguirPerfil(@RequestBody SeguirDTO seguirDTO) {
        try {
            perfilService.dejarDeSeguirPerfil(seguirDTO);
            return ResponseEntity.ok("Dejado de seguir exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/seguidores/{id}")
    public ResponseEntity<List<PerfilDTO>> obtenerSeguidores(@PathVariable Long id) {
        try {
            List<PerfilDTO> seguidores = perfilService.obtenerSeguidores(id);
            return ResponseEntity.ok(seguidores);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/seguidos/{id}")
    public ResponseEntity<List<PerfilDTO>> obtenerSeguidos(@PathVariable Long id) {
        try {
            List<PerfilDTO> seguidos = perfilService.obtenerSeguidos(id);
            return ResponseEntity.ok(seguidos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/es-seguidor")
    public ResponseEntity<Boolean> esSeguidor(@RequestBody SeguirDTO seguidorDTO) {
        boolean esSeguidor = perfilService.esSeguidor(seguidorDTO.getIdSeguidor(), seguidorDTO.getIdSeguido());
        return ResponseEntity.ok(esSeguidor);
    }

    @PostMapping("/favoritos/{idPerfil}/{idProducto}")
    public ResponseEntity<?> guardarFavorito(@PathVariable Long idPerfil, @PathVariable Long idProducto) {
        try {
            Favoritos favoritos = favoritosService.guardarFavorito(idPerfil, idProducto);
            return ResponseEntity.ok(favoritos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/favoritos/{idPerfil}")
    public ResponseEntity<List<Favoritos>> getFavoritosByPerfil(@PathVariable Long idPerfil) {
        List<Favoritos> favoritos = favoritosService.getById(idPerfil);
        return ResponseEntity.ok(favoritos);
    }

    @DeleteMapping("/favoritos/eliminar/{idPerfil}/{idProducto}")
    public ResponseEntity<?> eliminarFavorito(@PathVariable Long idPerfil, @PathVariable Long idProducto) {
        try {
            favoritosService.eliminarFavorito(idPerfil, idProducto);
            return ResponseEntity.ok("Favorito eliminado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/favoritos/comprobar/{idPerfil}/{idProducto}")
    public ResponseEntity<Boolean> esFavorito(@PathVariable Long idPerfil, @PathVariable Long idProducto) {
        try {
            boolean esFavorito = favoritosService.esFavorito(idPerfil, idProducto);
            return ResponseEntity.ok(esFavorito);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
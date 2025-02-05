package com.hunt2hand.service;

import com.hunt2hand.model.Favoritos;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Producto;
import com.hunt2hand.repository.FavoritosRepository;
import com.hunt2hand.repository.PerfilRepository;
import com.hunt2hand.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class FavoritosService {

    private final FavoritosRepository favoritosRepository;
    private final PerfilRepository perfilRepository;
    private final ProductoRepository productoRepository;

    public Favoritos añadirFavorito(Long idPerfil, Long idProducto) {
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new RuntimeException("Perfil con id " + idPerfil + " no encontrado"));
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto con id " + idProducto + " no encontrado"));

        if (favoritosRepository.existsByPerfilAndProducto(perfil, producto)) {
            throw new RuntimeException("El producto ya está en la lista de favoritos del perfil");
        }

        Favoritos favoritos = new Favoritos();
        favoritos.setPerfil(perfil);
        favoritos.setProducto(producto);

        return favoritosRepository.save(favoritos);
    }

    public void eliminarFavorito(Long idPerfil, Long idProducto) {
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new RuntimeException("Perfil con id " + idPerfil + " no encontrado"));
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto con id " + idProducto + " no encontrado"));

        Favoritos favorito = favoritosRepository.findByPerfilAndProducto(perfil, producto)
                .orElseThrow(() -> new RuntimeException("El favorito no existe"));

        favoritosRepository.delete(favorito);
    }

    public List<Favoritos> getFavoritosByPerfil(Long idPerfil) {
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new RuntimeException("Perfil con id " + idPerfil + " no encontrado"));
        return favoritosRepository.findByPerfil(perfil);
    }

    public boolean esFavorito(Long idPerfil, Long idProducto) {
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new RuntimeException("Perfil con id " + idPerfil + " no encontrado"));
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto con id " + idProducto + " no encontrado"));

        return favoritosRepository.existsByPerfilAndProducto(perfil, producto);
    }
}
package com.hunt2hand.repository;

import com.hunt2hand.model.Favoritos;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritosRepository extends JpaRepository<Favoritos, Long> {
    List<Favoritos> findByPerfil_Id(Long id);
    List<Favoritos> findByProducto_Id(Long id);
    List<Favoritos> findByPerfil(Perfil perfil);
    boolean existsByPerfilAndProducto(Perfil perfil, Producto producto);
    Optional<Favoritos> findByPerfilAndProducto(Perfil perfil, Producto producto);

    List<Favoritos> id(Long id);
}

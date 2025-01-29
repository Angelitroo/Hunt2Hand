package com.hunt2hand.repository;

import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Producto;
import com.hunt2hand.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    List<Perfil> findByNombreLikeIgnoreCase(String nombre);

    Perfil findTopByUsuario(Usuario usuario);

    @Query("SELECT p FROM Perfil p WHERE p.usuario.username = :username")
    Optional<Perfil> findByUsuarioUsername(@Param("username") String username);


}
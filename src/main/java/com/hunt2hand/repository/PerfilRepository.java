package com.hunt2hand.repository;

import com.hunt2hand.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByNombre(String nombre);

    @Query("SELECT p FROM Perfil p WHERE p.usuario.username = :username")
    Optional<Perfil> findByUsuarioUsername(@Param("username") String username);


}
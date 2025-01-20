package com.hunt2hand.repository;

import com.hunt2hand.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {


    @Query("SELECT p FROM Perfil p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
            "OR LOWER(p.apellido) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    List<Perfil> buscar(@Param("busqueda") String busqueda);

    @Query("SELECT p FROM Perfil p WHERE p.usuario.username = :username")
    Optional<Perfil> findByUsuarioUsername(@Param("username") String username);


}
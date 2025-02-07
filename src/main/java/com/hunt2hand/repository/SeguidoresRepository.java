package com.hunt2hand.repository;

import com.hunt2hand.model.Seguidores;
import com.hunt2hand.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeguidoresRepository extends JpaRepository<Seguidores, Long> {
    List<Seguidores> findBySeguido(Perfil seguido);
    List<Seguidores> findBySeguidor(Perfil seguidor);
    boolean existsBySeguidorAndSeguido(Perfil seguidor, Perfil seguido);
    Optional<Seguidores> findBySeguidorAndSeguido(Perfil seguidor, Perfil seguido);
}
package com.hunt2hand.repository;

import com.hunt2hand.model.Chat;
import com.hunt2hand.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE (c.creador = :perfil1 AND c.receptor = :perfil2) OR (c.creador = :perfil2 AND c.receptor = :perfil1)")
    Optional<Chat> findChatBetweenUsers(@Param("perfil1") Perfil perfil1, @Param("perfil2") Perfil perfil2);

    @Query("SELECT c FROM Chat c WHERE c.creador.id = :userId OR c.receptor.id = :userId")
    List<Chat> findChatsByUserId(@Param("userId") Long userId);
}

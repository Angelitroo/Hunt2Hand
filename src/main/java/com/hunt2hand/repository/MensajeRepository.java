package com.hunt2hand.repository;

import com.hunt2hand.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    @Query("SELECT m FROM Mensaje m WHERE m.chat.id = :chatId ORDER BY m.fecha")
    List<Mensaje> findMessagesByChatId(@Param("chatId") Long chatId);

    @Query("SELECT m FROM Mensaje m WHERE m.emisor.id = :userId ORDER BY m.fecha")
    List<Mensaje> findSentMessages(@Param("userId") Long userId);

    @Query("SELECT m FROM Mensaje m WHERE m.receptor.id = :userId ORDER BY m.fecha")
    List<Mensaje> findReceivedMessages(@Param("userId") Long userId);
}

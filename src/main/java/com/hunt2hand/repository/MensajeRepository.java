package com.hunt2hand.repository;

import com.hunt2hand.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    @Query("SELECT m FROM Mensaje m WHERE m.chat.id = :chatId ORDER BY m.fecha ASC")
    List<Mensaje> findMessagesByChatId(@Param("chatId") Long chatId);

    @Query("SELECT m FROM Mensaje m WHERE m.emisor.id = :userId ORDER BY m.fecha ASC")
    List<Mensaje> findSentMessages(@Param("userId") Long userId);

    @Query("SELECT m FROM Mensaje m WHERE m.receptor.id = :userId ORDER BY m.fecha ASC")
    List<Mensaje> findReceivedMessages(@Param("userId") Long userId);

    @Query("SELECT m FROM Mensaje m " +
            "JOIN Perfil u ON (m.emisor.id = :idPerfil2 AND m.receptor.id = :idPerfil1) OR " +
            "(m.receptor.id = :idPerfil2 AND m.emisor.id = :idPerfil1) " +
            "ORDER BY m.fecha ASC") // Cambiar a ASC para ordenar de más antiguo a más reciente
    List<Mensaje> getMensajesPorPerfil(@Param("idPerfil1") Integer idPerfil1, @Param("idPerfil2") Integer idPerfil2);


}

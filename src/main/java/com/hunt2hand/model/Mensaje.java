package com.hunt2hand.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "mensaje", schema = "hunt2hand")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "id_emisor", nullable = false)
    private Perfil emisor;

    @ManyToOne
    @JoinColumn(name = "id_receptor", nullable = false)
    private Perfil receptor;

    @Column(name = "contenido", nullable = false)
    private String contenido;

    @Column(name = "fecha")
    private LocalDateTime fecha;
}

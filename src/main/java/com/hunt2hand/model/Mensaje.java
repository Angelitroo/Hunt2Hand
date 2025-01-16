package com.hunt2hand.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.LocalDate;
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
    @JoinColumn(name = "id_chat", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;

    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "hora")
    private LocalTime hora;
}

package com.hunt2hand.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reporte", schema = "hunt2hand")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha")
    private LocalDate fecha;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_reportador" ,nullable = false)
    private Perfil reportador;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_reportado", nullable = false)
    private Perfil reportado;

    @Column(name = "motivo")
    private String motivo;
}

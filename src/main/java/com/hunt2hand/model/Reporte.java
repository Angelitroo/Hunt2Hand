package com.hunt2hand.model;

import com.hunt2hand.enums.Motivo;
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

    @ManyToOne
    @JoinColumn(name = "id_reportador")
    private Perfil reportador;

    @ManyToOne
    @JoinColumn(name = "id_reportado")
    private Perfil reportado;

    @Column(name = "motivo")
    private Motivo motivo;
}

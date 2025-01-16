package com.hunt2hand.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Id
    @ManyToOne
    @JoinColumn(name = "id_reportador")
    private Perfil reportador;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_reportado")
    private Perfil reportado;

    @Column(name = "motivo")
    private String motivo;
}

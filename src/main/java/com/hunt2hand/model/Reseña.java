package com.hunt2hand.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reseña", schema = "hunt2hand")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Reseña {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "valoracion")
    private Integer valoracion;

    @ManyToOne
    @JoinColumn(name = "id_perfilvalorado", nullable = false)
    private Perfil perfilValorado;

    @OneToOne
    @JoinColumn(name = "id_perfilvalorador", nullable = false)
    private Perfil perfilValorador;



}

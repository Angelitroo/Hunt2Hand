package com.hunt2hand.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "favoritos", schema = "hunt2hand")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Favoritos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;

    @OneToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;
}

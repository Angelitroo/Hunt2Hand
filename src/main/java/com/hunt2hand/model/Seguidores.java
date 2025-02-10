package com.hunt2hand.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seguidores", schema = "hunt2hand")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Seguidores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_seguidor")
    private Perfil seguidor;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_seguido")
    private Perfil seguido;

}
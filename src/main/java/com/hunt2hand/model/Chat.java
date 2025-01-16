package com.hunt2hand.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat", schema = "hunt2hand")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_perfil1")
    private Perfil perfil1;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_perfil2")
    private Perfil perfil2;
}

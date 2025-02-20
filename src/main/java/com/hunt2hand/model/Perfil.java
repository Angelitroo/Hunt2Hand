package com.hunt2hand.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "perfil", schema = "hunt2hand")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class  Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "ubicacion")
    private String ubicacion;

    @Column(name = "imagen")
    private String imagen;

    @Column(name = "activado")
    private boolean activado;

    @Column(name = "baneado")
    private boolean baneado;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
}



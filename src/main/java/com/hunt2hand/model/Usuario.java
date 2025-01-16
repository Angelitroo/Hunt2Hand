package com.hunt2hand.model;


import com.hunt2hand.enums.Rol;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "usuario", schema = "hunt2hand")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class  Usuario
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "contraseña")
    private String contraseña;

    @Column(name = "rol")
    private Rol rol;
}



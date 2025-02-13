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

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_producto", nullable = false, foreignKey = @ForeignKey(name = "fk_favorito_producto", foreignKeyDefinition = "FOREIGN KEY (id_producto) REFERENCES hunt2hand.producto(id) ON DELETE CASCADE"))
    private Producto producto;
}


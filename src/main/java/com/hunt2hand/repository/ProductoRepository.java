package com.hunt2hand.repository;

import com.hunt2hand.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByNombreLikeIgnoreCase(String nombre);
    List<Producto> findByPerfilId(Long idPerfil);

}

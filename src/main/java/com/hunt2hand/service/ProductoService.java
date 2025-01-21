package com.hunt2hand.service;

import com.hunt2hand.dto.ProductoDTO;
import com.hunt2hand.model.Producto;
import com.hunt2hand.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;

    public List<ProductoDTO> getAll(Producto producto) {
        List<Producto> productos = productoRepository.findAll();

        if (productos == null) {
            return Collections.emptyList();
        }

        return productos.stream()
        .map (Producto -> {
            ProductoDTO dto = new ProductoDTO();
            dto.setId(Producto.getId());
            dto.setNombre(Producto.getNombre());
            dto.setDescripcion(Producto.getDescripcion());
            dto.setPrecio(Producto.getPrecio());
            dto.setEstado(Producto.getEstado().name());
            dto.setImagen(Producto.getImagen());
            dto.setVendido(Producto.getVendido());
            dto.setPerfil(Producto.getPerfil().getId());
            return dto;
        })
        .collect(Collectors.toList());
    }

    public ProductoDTO getById(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);

        if (producto == null) {
            return null;
        }

        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setEstado(producto.getEstado().name());
        dto.setImagen(producto.getImagen());
        dto.setVendido(producto.getVendido());
        dto.setPerfil(producto.getPerfil().getId());
        return dto;
    }

    public ProductoDTO getByName(String Name) {
        Producto producto = productoRepository.findByNombre(Name);

        if (producto == null) {
            return null;
        }

        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setEstado(producto.getEstado().name());
        dto.setImagen(producto.getImagen());
        dto.setVendido(producto.getVendido());
        dto.setPerfil(producto.getPerfil().getId());
        return dto;
    }

}

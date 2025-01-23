package com.hunt2hand.service;

import com.hunt2hand.dto.ProductoDTO;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Producto;
import com.hunt2hand.repository.PerfilRepository;
import com.hunt2hand.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final PerfilRepository perfilRepository;

    public List<ProductoDTO> getAll() {
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
            dto.setEstado(Producto.getEstado());
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
        dto.setEstado(producto.getEstado());
        dto.setImagen(producto.getImagen());
        dto.setVendido(producto.getVendido());
        dto.setPerfil(producto.getPerfil().getId());
        return dto;
    }

    public ProductoDTO getByNombre(String nombre) {
        Producto producto = productoRepository.findByNombre(nombre).orElse(null);

        if (producto == null) {
            return null;
        }

        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setEstado(producto.getEstado());
        dto.setImagen(producto.getImagen());
        dto.setVendido(producto.getVendido());
        dto.setPerfil(producto.getPerfil().getId());
        return dto;
    }

    public ProductoDTO guardar(ProductoDTO productoDTO, Long idPerfil) {
        Perfil perfil = perfilRepository.findById(idPerfil).orElseThrow(() -> new RuntimeException("Viaje con id " + idPerfil + " no encontrado"));

        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setEstado(productoDTO.getEstado());
        producto.setImagen(productoDTO.getImagen());
        producto.setVendido(productoDTO.getVendido());
        producto.setPerfil(perfil);

        Producto productoGuardado = productoRepository.save(producto);

        ProductoDTO dto = new ProductoDTO();
        dto.setId(productoGuardado.getId());
        dto.setNombre(productoGuardado.getNombre());
        dto.setDescripcion(productoGuardado.getDescripcion());
        dto.setPrecio(productoGuardado.getPrecio());
        dto.setEstado(productoGuardado.getEstado());
        dto.setImagen(productoGuardado.getImagen());
        dto.setVendido(productoGuardado.getVendido());
        dto.setPerfil(productoGuardado.getPerfil().getId());

        return dto;
    }









    public String eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new IllegalArgumentException("El id no existe");
        }
        productoRepository.deleteById(id);
        return "Eliminado correctamente";
    }
}

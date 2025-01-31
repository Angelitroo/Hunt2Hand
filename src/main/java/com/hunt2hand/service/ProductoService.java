package com.hunt2hand.service;

import com.hunt2hand.dto.PerfilDTO;
import com.hunt2hand.dto.ProductoDTO;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Producto;
import com.hunt2hand.repository.PerfilRepository;
import com.hunt2hand.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductoDTO getById(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);

        if (producto == null) {
            return null;
        }

        return convertToDto(producto);
    }

    public List<ProductoDTO> getByNombre(String nombre) {
        String patron = nombre + "%";

        List<Producto> productos = productoRepository.findByNombreLikeIgnoreCase(patron);

        if (productos == null) {
            return Collections.emptyList();
        }

        return productos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductoDTO guardar(ProductoDTO productoDTO, Long idPerfil) {
        Perfil perfil = perfilRepository.findById(idPerfil).orElseThrow(() -> new RuntimeException("Usuario con id " + idPerfil + " no encontrado"));

        Producto producto = new Producto();
        producto.setId(productoDTO.getId());
        producto.setNombre(productoDTO.getNombre());
        producto.setCategoria(productoDTO.getCategoria());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setEstado(productoDTO.getEstado());
        producto.setImagen(productoDTO.getImagen());
        producto.setVendido(productoDTO.getVendido());
        producto.setPerfil(perfil);

        Producto productoGuardado = productoRepository.save(producto);

        return convertToDto(productoGuardado);
    }

    public ProductoDTO actualizar(ProductoDTO productoDTO, Long idProducto) {
        Producto producto = productoRepository.findById(idProducto).orElseThrow(() -> new IllegalArgumentException("El id no existe"));

        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setEstado(productoDTO.getEstado());
        producto.setImagen(productoDTO.getImagen());
        producto.setVendido(productoDTO.getVendido());

        Producto productoActualizado = productoRepository.save(producto);

        return convertToDto(productoActualizado);
    }

    public String eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new IllegalArgumentException("El id no existe");
        }
        productoRepository.deleteById(id);
        return "Eliminado correctamente";
    }

    public List<ProductoDTO> getByPerfilId(Long idPerfil) {
        List<Producto> productos = productoRepository.findByPerfilId(idPerfil);
        return productos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductoDTO convertToDto(Producto producto) {
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

}

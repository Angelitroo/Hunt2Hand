package com.hunt2hand.service;

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

    public ProductoDTO getByNombre(String Name) {
        Producto producto = productoRepository.findByNombre(Name);

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

    public Set<ProductoDTO> getByCategoria(String categoria) {
        Set<ProductoDTO> productoDTOS = new HashSet<>();
        for (Producto producto : productoRepository.findByCategoria(categoria)) {
            ProductoDTO productoDTO = new ProductoDTO();
            productoDTO.setId(producto.getId());
            productoDTO.setNombre(producto.getNombre());
            productoDTO.setCategoria(producto.getCategoria());
            productoDTO.setDescripcion(producto.getDescripcion());
            productoDTO.setPrecio(producto.getPrecio());
            productoDTO.setEstado(producto.getEstado());
            productoDTO.setImagen(producto.getImagen());
            productoDTO.setVendido(producto.getVendido());
            productoDTO.setPerfil(producto.getPerfil().getId());
            productoDTOS.add(productoDTO);
        }
        return productoDTOS;
    }

    public ProductoDTO guardar(ProductoDTO productoDTO, Long idPerfil) {
        Perfil perfil = perfilRepository.findById(Math.toIntExact(idPerfil)).orElseThrow(() -> new RuntimeException("Viaje con id " + idPerfil + " no encontrado"));

        Producto producto = new Producto();
        producto.setNombre(producto.getNombre());
        producto.setDescripcion(producto.getDescripcion());
        producto.setPrecio(producto.getPrecio());
        producto.setEstado(producto.getEstado());
        producto.setImagen(producto.getImagen());
        producto.setVendido(producto.getVendido());
        producto.setPerfil(perfil);

        Producto productoGuardado = productoRepository.save(producto);

        ProductoDTO dto = new ProductoDTO();
        dto.setNombre(productoGuardado.getNombre());
        dto.setDescripcion(productoGuardado.getDescripcion());
        dto.setPrecio(productoGuardado.getPrecio());
        dto.setEstado(productoGuardado.getEstado());
        dto.setImagen(productoGuardado.getImagen());
        dto.setVendido(productoGuardado.getVendido());
        dto.setPerfil(productoGuardado.getPerfil().getId());

        return dto;
    }

    public ProductoDTO actualizar(ProductoDTO productoDTO, Long idProducto, Long idPerfil) {
        Producto producto = productoRepository.findById(idProducto).orElseThrow(() -> new RuntimeException("Producto con id " + idProducto + " no encontrado"));

        Perfil perfil = perfilRepository.findById(Math.toIntExact(idPerfil)).orElseThrow(() -> new RuntimeException("Perfil con id " + idPerfil + " no encontrado"));

        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setEstado(productoDTO.getEstado());
        producto.setImagen(productoDTO.getImagen());
        producto.setVendido(productoDTO.getVendido());
        producto.setPerfil(perfil);

        Producto productoActualizado = productoRepository.save(producto);

        ProductoDTO dto = new ProductoDTO();
        dto.setId(productoActualizado.getId());
        dto.setNombre(productoActualizado.getNombre());
        dto.setDescripcion(productoActualizado.getDescripcion());
        dto.setPrecio(productoActualizado.getPrecio());
        dto.setEstado(productoActualizado.getEstado());
        dto.setImagen(productoActualizado.getImagen());
        dto.setVendido(productoActualizado.getVendido());
        dto.setPerfil(productoActualizado.getPerfil().getId());

        return dto;
    }

    public String eliminar(Long id) {
        productoRepository.deleteById(id);
        return "Producto eliminado correctamente";
    }
}

package com.hunt2hand.service;

import com.hunt2hand.dto.ProductoDTO;
import com.hunt2hand.exception.RecursoNoEncontrado;
import com.hunt2hand.model.Perfil;
import com.hunt2hand.model.Producto;
import com.hunt2hand.repository.PerfilRepository;
import com.hunt2hand.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@AllArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final PerfilRepository perfilRepository;

    public List<ProductoDTO> getAll() {
        List<Producto> productos = productoRepository.findAll();

        if (productos.isEmpty()) {
            throw new RecursoNoEncontrado("No se encontraron productos");
        }

        return productos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductoDTO getById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Producto con id " + id + " no encontrado"));

        return convertToDto(producto);
    }

    public List<ProductoDTO> getByNombre(String nombre) {
        String patron = nombre + "%";

        List<Producto> productos = productoRepository.findByNombreLikeIgnoreCase(patron);

        if (productos == null || productos.isEmpty()) {
            throw new RecursoNoEncontrado("No se encontraron productos con el nombre: " + nombre);
        }

        return productos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> getByCategoria(String categoria) {
        List<Producto> productos = productoRepository.findByCategoria(categoria);

        if (productos == null || productos.isEmpty()) {
            throw new RecursoNoEncontrado("No se encontraron productos en la categoría: " + categoria);
        }

        return productos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductoDTO guardar(ProductoDTO productoDTO, Long idPerfil) {
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario con id " + idPerfil + " no encontrado"));

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
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RecursoNoEncontrado("Producto con id " + idProducto + " no encontrado"));

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
            throw new RecursoNoEncontrado("Producto con id " + id + " no encontrado");
        }
        productoRepository.deleteById(id);
        return "Eliminado correctamente";
    }

    public List<ProductoDTO> getByPerfilId(Long idPerfil) {
        List<Producto> productos = productoRepository.findByPerfilId(idPerfil);

        if (productos == null || productos.isEmpty()) {
            throw new RecursoNoEncontrado("No se encontraron productos para el perfil con id: " + idPerfil);
        }

        return productos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductoDTO convertToDto(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setCategoria(producto.getCategoria());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setEstado(producto.getEstado());
        dto.setImagen(producto.getImagen());
        dto.setVendido(producto.getVendido());
        dto.setPerfil(producto.getPerfil().getId());
        return dto;
    }
}
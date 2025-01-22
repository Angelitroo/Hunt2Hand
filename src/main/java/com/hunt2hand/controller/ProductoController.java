package com.hunt2hand.controller;

import com.hunt2hand.dto.ProductoDTO;
import com.hunt2hand.dto.ReporteDTO;
import com.hunt2hand.model.Producto;
import com.hunt2hand.service.ProductoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/productos")
@AllArgsConstructor

public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/")
    public List<ProductoDTO> getAll(@RequestBody Producto producto) {
        return productoService.getAll(producto);
    }

    @GetMapping({"/{id}"})
    public ProductoDTO getById(@PathVariable Long id) {
        return productoService.getById(id);
    }

    @GetMapping({"/nombre/{nombre}"})
    public ProductoDTO getByNombre(@PathVariable String nombre) {
        return productoService.getByNombre(nombre);
    }


    @GetMapping({"/categoria/{categoria}"})
    public ResponseEntity<Set<ProductoDTO>> getByCategoria(@PathVariable String categoria) {
        Set<ProductoDTO> productos = productoService.getByCategoria(categoria);
        return ResponseEntity.ok(productos);
    }


    @PostMapping({"/guardar/{idPerfil}"})
    public ProductoDTO guardar(@RequestBody ProductoDTO producto, @PathVariable Long idPerfil) {
        return productoService.guardar(producto, idPerfil);
    }



    @DeleteMapping({"/eliminar"})
    public String eliminar(Long id) {
        return productoService.eliminar(id);
    }
}

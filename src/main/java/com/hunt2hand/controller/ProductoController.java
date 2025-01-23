package com.hunt2hand.controller;

import com.hunt2hand.dto.ProductoDTO;
import com.hunt2hand.model.Producto;
import com.hunt2hand.service.ProductoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@AllArgsConstructor

public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/")
    public List<ProductoDTO> getAll() {
        return productoService.getAll();
    }

    @GetMapping({"/{id}"})
    public ProductoDTO getById(@PathVariable Long id) {
        return productoService.getById(id);
    }

    @GetMapping({"/buscar/{nombre}"})
    public ResponseEntity<ProductoDTO> getProductoByNombre(@PathVariable String nombre) {
        ProductoDTO productoDTO = productoService.getByNombre(nombre);

        if (productoDTO == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productoDTO);
    }

    @PostMapping({"/guardar/{idPerfil}"})
    public ProductoDTO guardar(@RequestBody ProductoDTO producto, @PathVariable Long idPerfil) {
        return productoService.guardar(producto, idPerfil);
    }








    @DeleteMapping({"/eliminar/{id}"})
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        try {
            String resultado = productoService.eliminar(id);
            return ResponseEntity.ok(resultado);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

package com.datapar.ecommerce.Productos;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;



    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getAllProductos() {
        List<ProductoDTO> productos = productoService.getAllProductos();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
        ProductoDTO productoDTO = productoService.getProductoPorId(id);
        return new ResponseEntity<>(productoDTO, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ProductoDTO> addProducto(@RequestBody ProductoDTO productoDTO) {
        ProductoDTO nuevoProducto = productoService.crearproducto(productoDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarCategoria(@PathVariable Long id,
                                                            @RequestBody ProductoDTO productoDTO) {
        ProductoDTO productoActualizada =  productoService.actualizarProducto(id, productoDTO);

        return new ResponseEntity<>(productoActualizada, HttpStatus.OK);
    }

    @DeleteMapping("/id")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
    }



}

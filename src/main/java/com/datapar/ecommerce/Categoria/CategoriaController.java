package com.datapar.ecommerce.Categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> getAllCategorias() {
        List<CategoriaDTO> categorias = categoriaService.getAllCategorias();
        System.out.println("getAllCategorias llamado");
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> getCategoriaById(@PathVariable Long id) {
        CategoriaDTO categoriaDTO = categoriaService.getCategoriaPorId(id);
        return new ResponseEntity<>(categoriaDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> addCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        CategoriaDTO nuevaCategoria = categoriaService.crearCategoria(categoriaDTO);

        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> actualizarCategoria(@PathVariable Long id,
                                                            @RequestBody CategoriaDTO categoriaDTO) {
        CategoriaDTO categoriaActualizada =  categoriaService.actualizarCategoria(id, categoriaDTO);

        return new ResponseEntity<>(categoriaActualizada, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
    }
}

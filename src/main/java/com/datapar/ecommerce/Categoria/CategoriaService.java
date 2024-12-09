package com.datapar.ecommerce.Categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO) {
        Categoria nuevaCategoria = toCategoria(categoriaDTO);

        return toCategoriaDTO(categoriaRepository.save(nuevaCategoria));
    }

    public List<CategoriaDTO> getAllCategorias() {
        List<Categoria> categorias = categoriaRepository.findAllActive();
        ArrayList<CategoriaDTO> categoriasDTO = new ArrayList<>();

        for (Categoria categoria : categorias) {
            categoriasDTO.add(toCategoriaDTO(categoria));
        }

        return categoriasDTO;
    }

    public CategoriaDTO getCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findByIdActive(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        return toCategoriaDTO(categoria);
    }

    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        Categoria categoriaActual = categoriaRepository.findByIdActive(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        categoriaActual.setNombre(categoriaDTO.getNombre());
        categoriaActual.setDescripcion(categoriaDTO.getDescripcion());

        Categoria categoriaActualizada = categoriaRepository.save(categoriaActual);

        return toCategoriaDTO(categoriaActualizada);
    }

    public void eliminarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findByIdActive(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        categoria.setDeletedAt(new Date());
        categoriaRepository.save(categoria);
    }

    private Categoria toCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = new Categoria();

        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setDescripcion(categoriaDTO.getDescripcion());

        return categoria;
    }

    private CategoriaDTO toCategoriaDTO(Categoria categoria) {
        CategoriaDTO categoriaDTO = new CategoriaDTO();

        categoriaDTO.setId(categoria.getId());
        categoriaDTO.setNombre(categoria.getNombre());
        categoriaDTO.setDescripcion(categoria.getDescripcion());
        return categoriaDTO;
    }
}

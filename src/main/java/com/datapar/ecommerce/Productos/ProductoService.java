package com.datapar.ecommerce.Productos;



import com.datapar.ecommerce.Categoria.Categoria;
import com.datapar.ecommerce.Categoria.CategoriaDTO;
import com.datapar.ecommerce.Categoria.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<ProductoDTO> getAllProductos() {
        List<Producto> prductos = productoRepository.findAllActive();
        ArrayList<ProductoDTO> productoDTO = new ArrayList<>();

        for (Producto producto : prductos) {
            productoDTO.add(toProductoDTO(producto));
        }

        return productoDTO;
    }
    public ProductoDTO getProductoPorId(Long id) {
        Producto producto = productoRepository.findByIdActive(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        return toProductoDTO(producto);
    }

    public ProductoDTO crearproducto(ProductoDTO productoDTO) {
        Producto nuevoproducto = toProducto(productoDTO);
        return toProductoDTO(productoRepository.save(nuevoproducto));
    }


    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO) {
        Producto productoaActual = productoRepository.findByIdActive(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        productoaActual.setNombre(productoDTO.getNombre());
        productoaActual.setDescripcion(productoDTO.getDescripcion());
        productoaActual.setPrecio(productoDTO.getPrecio());
        productoaActual.setStock(productoDTO.getStock());
        productoaActual.setImagen(productoDTO.getImagen());
        productoRepository.save(productoaActual);

        Producto productoActualizado = productoRepository.save(productoaActual);

        return toProductoDTO(productoActualizado
        );
    }

    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findByIdActive(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setDeletedAt(new Date());
        productoRepository.save(producto);
    }





    private Producto toProducto(ProductoDTO productoDTO) {
        Producto producto = new Producto();

        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setStock(productoDTO.getStock());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setImagen(productoDTO.getImagen());
        System.out.println(productoDTO.getCategoriaId());
        producto.setCategoria(categoriaRepository.findByIdActive(productoDTO.getCategoriaId()).get());
        return producto;
    }

    private ProductoDTO toProductoDTO(Producto producto) {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(producto.getId());
        productoDTO.setNombre(producto.getNombre());
        productoDTO.setDescripcion(producto.getDescripcion());
        productoDTO.setPrecio(producto.getPrecio());
        productoDTO.setStock(producto.getStock());
        productoDTO.setImagen(producto.getImagen());
        productoDTO.setCategoriaId(producto.getCategoria().getId());
        return productoDTO;

    }




}

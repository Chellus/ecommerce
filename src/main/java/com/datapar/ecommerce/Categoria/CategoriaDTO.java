package com.datapar.ecommerce.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaDTO {
    private Long id;
    private String nombre;
    private String descripcion;

    private List<Long> productosId = new ArrayList<>();

    public CategoriaDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

package com.datapar.ecommerce.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    @Query("SELECT c FROM Categoria c WHERE c.deletedAt IS NULL")
    List<Categoria> findAllActive();

    @Query("SELECT c FROM Categoria c WHERE c.id = :id AND c.deletedAt IS NULL")
    Optional<Categoria> findByIdActive(Long id);
}

package com.datapar.ecommerce.Productos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Query("SELECT c FROM Producto c WHERE c.deletedAt IS NULL")
    List<Producto> findAllActive();

    @Query("SELECT c FROM Producto c WHERE c.id = :id AND c.deletedAt IS NULL")
    Optional<Producto> findByIdActive(Long id);
}

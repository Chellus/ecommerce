package com.datapar.ecommerce.Pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Query("SELECT p FROM Pedido p WHERE p.cliente = :clienteId AND p.deletedAt IS NULL")
    Optional<List<Pedido>> findByClienteId(Long clienteId);

    @Query("SELECT p FROM Pedido p WHERE p.id = :id AND p.cliente = :clienteId AND p.deletedAt IS NULL")
    Optional<Pedido> findByIdAndClienteId(Long id, Long clienteId);

    @Query("SELECT p FROM Pedido p WHERE p.id = :id AND p.deletedAt IS NULL")
    Optional<Pedido> findByIdActive(Long id);
}

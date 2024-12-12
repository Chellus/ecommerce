package com.datapar.ecommerce.DetallePedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    @Query("SELECT d FROM DetallePedido d WHERE d.pedido = :pedidoId AND d.deletedAt IS NULL")
    Optional<List<DetallePedido>> findByPedidoIdActive(Long pedidoId);
}

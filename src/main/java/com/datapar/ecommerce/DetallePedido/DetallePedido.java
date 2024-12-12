package com.datapar.ecommerce.DetallePedido;


import com.datapar.ecommerce.Pedido.Pedido;
import com.datapar.ecommerce.Productos.Producto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Pedido pedido;

    @ManyToOne
    private Producto producto;

    private int cantidad;

    private double subTotal;

    private Date deletedAt;
}

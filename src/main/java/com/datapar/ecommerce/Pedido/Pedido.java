package com.datapar.ecommerce.Pedido;

import com.datapar.ecommerce.DetallePedido.DetallePedido;
import com.datapar.ecommerce.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mapstruct.Mapping;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User cliente;

    private Date fecha;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles;

    private double total;

    private Date deletedAt;
}

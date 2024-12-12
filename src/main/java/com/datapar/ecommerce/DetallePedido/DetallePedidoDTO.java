package com.datapar.ecommerce.DetallePedido;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetallePedidoDTO {
    private Long id;

    private Long idPedido;

    private Long idProducto;

    private int cantidad;

    private double subTotal;
}

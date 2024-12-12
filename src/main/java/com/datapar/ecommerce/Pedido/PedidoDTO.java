package com.datapar.ecommerce.Pedido;

import com.datapar.ecommerce.DetallePedido.DetallePedidoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long pedidoId;

    private Long clienteId;

    private String estado;

    private List<DetallePedidoDTO> detalles = new ArrayList<>();

    private double total;

    private Date fecha;

    public void addDetalle(DetallePedidoDTO detalle) {
        detalles.add(detalle);
    }
}

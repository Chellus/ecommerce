package com.datapar.ecommerce.Pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/{clientId}")
    public ResponseEntity<List<PedidoDTO>> listar(@PathVariable Long clientId) {
        List<PedidoDTO> pedidos = pedidoService.findAllByClient(clientId);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{clientId}/{id}")
    public ResponseEntity<PedidoDTO> buscar(@PathVariable Long clientId, @PathVariable Long id) {
        PedidoDTO pedidoDTO = pedidoService.getById(id, clientId);
        return ResponseEntity.ok(pedidoDTO);
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> crear(@RequestBody PedidoDTO pedidoDTO) {
        PedidoDTO pedidoCreado = pedidoService.create(pedidoDTO);
        return ResponseEntity.ok(pedidoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> editar(@PathVariable Long id, @RequestBody PedidoDTO pedidoDTO) {
        PedidoDTO pedidoActualizado = pedidoService.update(id, pedidoDTO);
        return ResponseEntity.ok(pedidoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

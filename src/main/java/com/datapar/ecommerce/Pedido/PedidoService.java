package com.datapar.ecommerce.Pedido;

import com.datapar.ecommerce.DetallePedido.DetallePedido;
import com.datapar.ecommerce.DetallePedido.DetallePedidoDTO;
import com.datapar.ecommerce.DetallePedido.DetallePedidoRepository;
import com.datapar.ecommerce.Exceptions.AppException;
import com.datapar.ecommerce.Productos.Producto;
import com.datapar.ecommerce.Productos.ProductoRepository;
import com.datapar.ecommerce.User.User;
import com.datapar.ecommerce.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<PedidoDTO> findAllByClient(Long clienteId) {
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new AppException("Cliente no encontrado", HttpStatus.NOT_FOUND));
        List<PedidoDTO> pedidosDTO = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            pedidosDTO.add(toPedidoDTO(pedido));
        }

        return pedidosDTO;
    }

    public PedidoDTO getById(Long id, Long clienteId) {
        Pedido pedido = pedidoRepository.findByIdAndClienteId(id, clienteId)
                .orElseThrow(() -> new AppException("Pedido no encontrado", HttpStatus.NOT_FOUND));

        return toPedidoDTO(pedido);
    }

    @Transactional
    public PedidoDTO create(PedidoDTO pedidoDTO) {
        Pedido pedido = new Pedido();
        List<DetallePedido> detalles = new ArrayList<>();
        double total = 0.0;

        // buscamos al cliente que hizo el pedido
        User user = userRepository.findByIdActive(pedidoDTO.getClienteId())
                .orElseThrow(() -> new AppException("Cliente no encontrado", HttpStatus.NOT_FOUND));

        pedido.setCliente(user);

        // procesamos todos los productos en el pedido
        for (DetallePedidoDTO detallePedidoDTO : pedidoDTO.getDetalles()) {
            Producto producto = productoRepository.findByIdActive(detallePedidoDTO.getIdProducto())
                    .orElseThrow(() -> new AppException("Producto no encontrado", HttpStatus.NOT_FOUND));

            // revisamos si el producto tiene suficiente stock
            if (producto.getStock() < detallePedidoDTO.getCantidad()) {
                throw new AppException("Cantidad insuficiente", HttpStatus.BAD_REQUEST);
            }

            // actualizar el stock del producto
            producto.setStock(producto.getStock() - detallePedidoDTO.getCantidad());
            productoRepository.save(producto);

            // crear el detallePedido
            DetallePedido detallePedido = new DetallePedido();
            detallePedido.setPedido(pedido);
            detallePedido.setProducto(producto);
            detallePedido.setCantidad(detallePedidoDTO.getCantidad());
            detallePedido.setSubTotal(producto.getPrecio() * detallePedido.getCantidad());
            detalles.add(detallePedido);

            total += detallePedido.getSubTotal();
        }

        pedido.setDetalles(detalles);
        pedido.setTotal(total);
        pedido.setEstado(Estado.PENDIENTE);
        pedido.setFecha(new Date());
        pedidoRepository.save(pedido);

        return toPedidoDTO(pedido);
    }

    @Transactional
    public PedidoDTO update(Long id, PedidoDTO pedidoDTO) {
        // solamente podemos modificar una orden si su estado es pendiente
        // y solamente se puede modificar los productos que queremos y la cantidad de ellos
        Pedido pedidoExistente = pedidoRepository.findByIdActive(id)
                .orElseThrow(() -> new AppException("Pedido no encontrado", HttpStatus.NOT_FOUND));

        if (!pedidoExistente.getEstado().equals(Estado.PENDIENTE)) {
            throw new AppException("El pedido ya no se puede actualizar", HttpStatus.BAD_REQUEST);
        }

        double nuevoTotal = 0.0;
        List<DetallePedido> nuevosDetalles = new ArrayList<>();

         // procesamos cada producto en la orden
        for (DetallePedidoDTO detallePedidoDTO : pedidoDTO.getDetalles()) {
            Producto producto = productoRepository.findByIdActive(detallePedidoDTO.getIdProducto())
                    .orElseThrow(() -> new AppException("Producto no encontrado", HttpStatus.NOT_FOUND));

            // verificamos si el objeto ya se encontraba en la orden original
            DetallePedido detalleExistente = pedidoExistente.getDetalles().stream()
                    .filter(dp -> dp.getProducto()
                            .getId().equals(detallePedidoDTO.getIdProducto()))
                    .findFirst().orElse(null);

            // si este producto ya estaba en el pedido, revisamos la diferencia de cantidad
            int diferenciaCantidad = detallePedidoDTO.getCantidad() -
                    (detalleExistente != null ? detalleExistente.getCantidad() : 0);

            // revisamos si el producto tiene stock suficiente
            if (producto.getStock() < diferenciaCantidad) {
                throw new AppException("Cantidad insuficiente", HttpStatus.BAD_REQUEST);
            }

            // actualizamos el stock del producto
            producto.setStock(producto.getStock() - diferenciaCantidad);
            productoRepository.save(producto);

            // si el producto ya existia lo actualizamos
            if (detalleExistente != null) {
                detalleExistente.setCantidad(detallePedidoDTO.getCantidad());
                nuevosDetalles.add(detalleExistente);
            }
            else {
                // si no, creamos un nuevo DetallePedido
                DetallePedido detallePedido = new DetallePedido();
                detallePedido.setPedido(pedidoExistente);
                detallePedido.setProducto(producto);
                detallePedido.setCantidad(detallePedidoDTO.getCantidad());
                detallePedido.setSubTotal(producto.getPrecio() * detallePedidoDTO.getCantidad());
                pedidoExistente.getDetalles().add(detallePedido);
            }

            nuevoTotal += producto.getPrecio() * detallePedidoDTO.getCantidad();
        }

        // debemos quitar los productos que ya no se encuentren en la orden
        pedidoExistente.getDetalles().removeIf(dp ->
                nuevosDetalles.stream().noneMatch(newDp ->
                        newDp.getProducto().getId().equals(dp.getProducto().getId())));

        pedidoExistente.setTotal(nuevoTotal);

        return toPedidoDTO(pedidoExistente);
    }

    // para el proposito de esta app, borrar seria cancelar los pedidos
    @Transactional
    public void delete(Long id) {
        Pedido pedido = pedidoRepository.findByIdActive(id)
                .orElseThrow(() -> new AppException("Pedido no encontrado", HttpStatus.NOT_FOUND));
        // un pedido solamente se puede cancelar si aun no ha sido enviado
        if (!pedido.getEstado().equals(Estado.PENDIENTE)) {
            throw new AppException("El pedido no se puede cancelar", HttpStatus.BAD_REQUEST);
        }

        // restaurar el stock de los productos
        for (DetallePedido detallePedido : pedido.getDetalles()) {
            Producto producto = detallePedido.getProducto();
            producto.setStock(producto.getStock() + detallePedido.getCantidad());
            productoRepository.save(producto);
        }

        pedido.setDeletedAt(new Date());
        pedidoRepository.save(pedido);
    }

    private PedidoDTO toPedidoDTO(Pedido pedido) {
        PedidoDTO pedidoDTO = new PedidoDTO();
        List<DetallePedido> detallePedidos = detallePedidoRepository.findByPedidoIdActive(pedido.getId())
                .orElseThrow(() -> new AppException("Pedido no existe", HttpStatus.NOT_FOUND));
        pedidoDTO.setPedidoId(pedido.getId());
        pedidoDTO.setClienteId(pedido.getCliente().getId());

        double total = 0.0;

        for (DetallePedido detallePedido : detallePedidos) {
            DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO();
            detallePedidoDTO.setId(detallePedido.getId());
            detallePedidoDTO.setIdPedido(detallePedido.getPedido().getId());
            detallePedidoDTO.setIdProducto(detallePedido.getProducto().getId());
            detallePedidoDTO.setCantidad(detallePedido.getCantidad());
            detallePedidoDTO.setSubTotal(detallePedido.getCantidad() * detallePedido.getProducto().getPrecio());
            total += detallePedido.getCantidad() * detallePedido.getProducto().getPrecio();
            pedidoDTO.addDetalle(detallePedidoDTO);
        }

        pedidoDTO.setEstado(pedido.getEstado().name());
        pedidoDTO.setTotal(total);
        pedidoDTO.setFecha(pedido.getFecha());

        return pedidoDTO;

    }
}

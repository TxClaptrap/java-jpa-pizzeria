package ies.controlador;

import ies.modelo.Pedido;
import ies.modelo.LineaPedido;
import ies.modelo.Pagable;
import ies.modelo.Producto;

import java.sql.SQLException;
import java.util.List;

import ies.controlador.dao.PedidoDao;
import ies.controlador.dao.impl.JpaPedidoDao;
import ies.modelo.Cliente;
import ies.modelo.EstadoPedido;

public class ControladorPedido {
    private PedidoDao pedidoDao;

    public ControladorPedido() {
        pedidoDao = new JpaPedidoDao();
    }

    public void registrarPedido(Pedido pedido) throws SQLException {
        pedidoDao.insertPedido(pedido);
    }

    public void actualizarPedido(Pedido pedido) throws SQLException {
        pedidoDao.updatePedido(pedido);
    }

    public void borrarPedido(Pedido pedido) throws SQLException {
        pedidoDao.deletePedido(pedido);
    }

    public Pedido encontrarPedidoById(int idPedido) throws SQLException {
        return pedidoDao.findPedidoById(idPedido);
    }

    public List<Pedido> encontrarPedidosByClienteId(int clienteId) throws SQLException {
        return pedidoDao.findPedidosByClienteId(clienteId);
    }

    public List<Pedido> encontrarPedidosByEstado(EstadoPedido estado) throws SQLException {
        return pedidoDao.findPedidosByEstado(estado);
    }

    public List<LineaPedido> encontrarLineasPedidoByPedidoId(int pedidoId) throws SQLException {
        return pedidoDao.findLineasPedidoByPedidoId(pedidoId);
    }

    public void agregarLineaPedido(Producto producto, int cantidad, Cliente clienteActual)
            throws IllegalAccessException, SQLException {
        pedidoDao.addLineaPedido(producto, cantidad, clienteActual.getId());
    }

    public void cancelarPedido(Cliente clienteActual) throws IllegalAccessException, SQLException {
        if (clienteActual == null) {
            throw new IllegalAccessException("No se puede agregar pedido sin estar logeado.");
        }
        List<Pedido> listaPedidosPendientes = pedidoDao.findPedidosByEstado(EstadoPedido.PENDIENTE);
        for (Pedido pedido : listaPedidosPendientes) {
            if (pedido.getCliente().getId() == clienteActual.getId()) {
                pedido.setEstado(EstadoPedido.CANCELADO);
                pedidoDao.updatePedido(pedido);
                System.out.println("El pedido ha sido cancelado y eliminado de la base de datos.");
                return;
            }
        }
        System.out.println("No se ha podido cancelar el pedido.");
    }

    public void finalizarPedido(Cliente clienteActual, Pagable tipoPago) throws IllegalAccessException, SQLException {
        if (clienteActual == null) {
            throw new IllegalAccessException("No se puede finalizar un pedido sin estar logueado.");
        }
        List<Pedido> listaPedidosPendientes = pedidoDao.findPedidosByEstado(EstadoPedido.PENDIENTE);
        for (Pedido pedido : listaPedidosPendientes) {
            if (pedido.getCliente().getId() == clienteActual.getId()) {
                pedido.setMetodoPago(tipoPago);
                pedido.setEstado(EstadoPedido.FINALIZADO);
                pedidoDao.updatePedido(pedido);
                System.out.println(
                        "El pedido ha sido finalizado con el tipo de pago: " + tipoPago.getClass().getSimpleName());
                return;
            }
        }

        System.out.println("No se ha encontrado un pedido pendiente para este cliente.");
    }

    public void entregarPedido(Cliente clienteActual) throws SQLException, IllegalAccessException {
        if (clienteActual == null) {
            throw new IllegalAccessException("No se puede agregar pedido sin estar logeado.");
        }
        List<Pedido> listaPedidosPendientes = pedidoDao.findPedidosByEstado(EstadoPedido.FINALIZADO);
        for (Pedido pedido : listaPedidosPendientes) {
            if (pedido.getCliente().getId() == clienteActual.getId()) {
                pedido.setEstado(EstadoPedido.ENTREGADO);
                pedidoDao.updatePedido(pedido);
                System.out.println("El pedido ha sido entregado.");
                return;
            }
        }
        System.out.println("No se ha podido entregar el pedido.");
    }

}
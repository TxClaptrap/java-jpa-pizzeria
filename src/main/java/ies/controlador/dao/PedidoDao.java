package ies.controlador.dao;

import java.sql.SQLException;
import java.util.List;

import ies.modelo.EstadoPedido;
import ies.modelo.LineaPedido;
import ies.modelo.Pedido;

public interface PedidoDao {
    public void insertPedido(Pedido pedido ) throws SQLException;
    public void updatePedido(Pedido pedido) throws SQLException;
    public void deletePedido(Pedido pedido) throws SQLException;
    public Pedido findPedidoById(int idPedido) throws SQLException;
    public List<Pedido> findPedidosByClienteId(int ClienteId) throws SQLException;
    public List<Pedido> findPedidosByEstado(EstadoPedido estado) throws SQLException;
    public List<LineaPedido> findLineasPedidoByPedidoId(int PedidpId) throws SQLException;

}

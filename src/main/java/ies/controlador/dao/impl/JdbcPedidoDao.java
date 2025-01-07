package ies.controlador.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ies.controlador.dao.ClienteDao;
import ies.controlador.dao.PedidoDao;
import ies.controlador.dao.ProductoDao;
import ies.modelo.Cliente;
import ies.modelo.EstadoPedido;
import ies.modelo.LineaPedido;
import ies.modelo.Pagable;
import ies.modelo.Pagar_Efectivo;
import ies.modelo.Pagar_Tarjeta;
import ies.modelo.Pedido;
import ies.modelo.Producto;
import ies.utils.DatabaseConf;

public class JdbcPedidoDao implements PedidoDao {

    ClienteDao clienteDao = new JdbcClienteDao();
    ProductoDao productoDao = new JdbcProductoDao();

    @Override
    public void insertPedido(Pedido pedido) throws SQLException {
        String INSERT_PEDIDO = "INSERT INTO pedidos (fecha, precio_total, cliente_id, estado, pago) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conexion = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD)) {
            conexion.setAutoCommit(false);
    
            try (PreparedStatement pstmtPedido = conexion.prepareStatement(INSERT_PEDIDO, Statement.RETURN_GENERATED_KEYS)) {
                // Insertar el pedido
                pstmtPedido.setDate(1, Date.valueOf(pedido.getFecha()));
                pstmtPedido.setDouble(2, pedido.getPrecioTotal());
                pstmtPedido.setInt(3, pedido.getCliente().getId());
                pstmtPedido.setString(4, pedido.getEstado().name());  
                if (pedido.getMetodoPago() instanceof Pagar_Tarjeta) {
                    pstmtPedido.setString(5, "PAGAR_TARJETA");
                } else if (pedido.getMetodoPago() instanceof Pagar_Efectivo) {
                    pstmtPedido.setString(5, "PAGAR_EFECTIVO");
                } else {
                    pstmtPedido.setString(5, null);
                }
                pstmtPedido.executeUpdate();
    
                // Obtener el ID generado
                ResultSet generatedKeys = pstmtPedido.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int pedidoId = generatedKeys.getInt(1);
                    pedido.setId(pedidoId);
    
                    // Insertar las líneas del pedido
                    insertLineasPedido(conexion, pedidoId, pedido.getListaLineaPedidos());
                    System.out.println("Pedido insertado correctamente.");
                } else {
                    throw new SQLException("No se pudo obtener el ID del pedido insertado.");
                }
    
                conexion.commit();
            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            } finally {
                conexion.setAutoCommit(true);
            }
        }
    }
    

    private void insertLineasPedido(Connection conexion, int pedidoId, List<LineaPedido> lineasPedido)
            throws SQLException {
        String INSERT_LINEA_PEDIDO = "INSERT INTO lineasPedido (cantidad, precio, pedido_id, producto_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmtLinea = conexion.prepareStatement(INSERT_LINEA_PEDIDO)) {
            for (LineaPedido linea : lineasPedido) {
                pstmtLinea.setInt(1, linea.getCantidad());
                pstmtLinea.setDouble(2, linea.getPrecio());
                pstmtLinea.setInt(3, pedidoId);
                pstmtLinea.setInt(4, linea.getProducto().getId());
                pstmtLinea.executeUpdate();
            }
        }
    }

    @Override
    public void updatePedido(Pedido pedido) throws SQLException {
        String UPDATE_PEDIDO = "UPDATE pedidos SET fecha = ?, precio_total = ?, cliente_id = ?, estado = ?, pago = ? WHERE id = ?";
        String DELETE_LINEAS_PEDIDO = "DELETE FROM lineasPedido WHERE pedido_id = ?";
    
        try (Connection conexion = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD)) {
            conexion.setAutoCommit(false);
    
            try (PreparedStatement pstmtUpdatePedido = conexion.prepareStatement(UPDATE_PEDIDO);
                 PreparedStatement pstmtDeleteLineas = conexion.prepareStatement(DELETE_LINEAS_PEDIDO)) {
    
                // Actualizar el pedido principal
                pstmtUpdatePedido.setDate(1, Date.valueOf(pedido.getFecha()));
                pstmtUpdatePedido.setDouble(2, pedido.getPrecioTotal());
                pstmtUpdatePedido.setInt(3, pedido.getCliente().getId());
                pstmtUpdatePedido.setString(4, pedido.getEstado().name());
                if (pedido.getMetodoPago() instanceof Pagar_Tarjeta) {
                    pstmtUpdatePedido.setString(5, "PAGAR_TARJETA");
                } else if (pedido.getMetodoPago() instanceof Pagar_Efectivo) {
                    pstmtUpdatePedido.setString(5, "PAGAR_EFECTIVO");
                } else {
                    pstmtUpdatePedido.setString(5, null);
                }

                pstmtUpdatePedido.setInt(6, pedido.getId());
                pstmtUpdatePedido.executeUpdate();
    
                // Eliminar las líneas de pedido que ya hay
                pstmtDeleteLineas.setInt(1, pedido.getId());
                pstmtDeleteLineas.executeUpdate();
    
                // Insertar las nuevas líneas
                if (!pedido.getListaLineaPedidos().isEmpty()) {
                    insertLineasPedido(conexion, pedido.getId(), pedido.getListaLineaPedidos());
                }
                
    
                conexion.commit();
            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            } finally {
                conexion.setAutoCommit(true);
            }
        }
    }
    

    @Override
    public void deletePedido(Pedido pedido) throws SQLException {
        String DELETE_LINEAS_PEDIDO = "DELETE FROM lineaspedido WHERE pedido_id = ?";
        String DELETE_PEDIDO = "DELETE FROM pedidos WHERE id = ?";

        try (Connection conexion = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER,
                DatabaseConf.PASSWORD)) {
            // Iniciar la transacción
            conexion.setAutoCommit(false);

            try (PreparedStatement pstmtLineas = conexion.prepareStatement(DELETE_LINEAS_PEDIDO);
                    PreparedStatement pstmtPedido = conexion.prepareStatement(DELETE_PEDIDO)) {

                // Eliminar líneas asociadas
                pstmtLineas.setInt(1, pedido.getId());
                pstmtLineas.executeUpdate();

                // Eliminar pedido
                pstmtPedido.setInt(1, pedido.getId());
                int rowsAffected = pstmtPedido.executeUpdate();

                if (rowsAffected == 0) {
                    throw new SQLException("No se encontró el pedido con ID: " + pedido.getId());
                }

                // Confirmar transacción
                conexion.commit();
            } catch (SQLException e) {
                // Revertir en caso de error
                conexion.rollback();
                throw e;
            }
        }
    }

    @Override
    public Pedido findPedidoById(int idPedido) throws SQLException {
        String FIND_PEDIDO_BY_ID = "SELECT pedidos.id, pedidos.fecha, pedidos.precio_total, pedidos.cliente_id, pedidos.pago, pedidos.estado FROM pedidos LEFT JOIN lineaspedido ON pedidos.id = lineaspedido.pedido_id WHERE pedidos.id = ?";
        Pedido pedido = null;
    
        try (Connection conexion = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement pstmt = conexion.prepareStatement(FIND_PEDIDO_BY_ID)) {
    
            pstmt.setInt(1, idPedido);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                int clienteId = rs.getInt("cliente_id");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                double precioTotal = rs.getDouble("precio_total");
                EstadoPedido estado = EstadoPedido.valueOf(rs.getString("estado"));
                String pagoString = rs.getString("pago");
                Pagable pago = null; 
                if (pagoString != null) {
                    if (pagoString.equals("Pagar_Tarjeta")) {
                        pago = new Pagar_Tarjeta();
                    }
                    else {
                        pago = new Pagar_Efectivo();
                    }
                }

    
                Cliente cliente = clienteDao.findClienteById(clienteId);
    
                // Crear pedido
                pedido = new Pedido(cliente);
                pedido.setId(idPedido);
                pedido.setFecha(fecha);
                pedido.setPrecioTotal(precioTotal);
                pedido.setEstado(estado);
                pedido.setMetodoPago(pago); 
    
                // Recuperar líneas asociadas
                List<LineaPedido> lineasPedido = findLineasPedidoByPedidoId(idPedido);
                pedido.setListaLineaPedidos(lineasPedido);
            }
        }
    
        return pedido;
    }
    

    @Override
    public List<Pedido> findPedidosByClienteId(int clienteId) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String FIND_PEDIDOS_BY_CLIENTE_ID = "SELECT pedidos.id, pedidos.fecha, pedidos.estado, pedidos.precio_total " +
                "FROM pedidos " +
                "WHERE pedidos.cliente_id = ?";

        try (Connection conexion = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER,
                DatabaseConf.PASSWORD);
                PreparedStatement stmt = conexion.prepareStatement(FIND_PEDIDOS_BY_CLIENTE_ID)) {

            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                String estadoString = rs.getString("estado");
                EstadoPedido estado = EstadoPedido.valueOf(estadoString);
                double precioTotal = rs.getDouble("precio_total");

                // Recuperar cliente
                Cliente cliente = clienteDao.findClienteById(clienteId);

                // Crear Pedido
                Pedido pedido = new Pedido(id, cliente);
                pedido.setFecha(fecha);
                pedido.setEstado(estado);
                pedido.setPrecioTotal(precioTotal);

                pedidos.add(pedido);
            }
        }
        return pedidos;
    }

    @Override
    public List<Pedido> findPedidosByEstado(EstadoPedido estado) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String FIND_BY_ESTADO = "SELECT pedidos.id, pedidos.fecha, pedidos.precio_total, pedidos.cliente_id, pedidos.pago "
                +
                "FROM pedidos " +
                "WHERE pedidos.estado = ?";

        try (Connection conexion = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER,
                DatabaseConf.PASSWORD);
                PreparedStatement pstmt = conexion.prepareStatement(FIND_BY_ESTADO)) {

            pstmt.setString(1, estado.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                double precioTotal = rs.getDouble("precio_total");
                int clienteId = rs.getInt("cliente_id");
                String pagoString = rs.getString("pago");
                Pagable pago = null; 
                if (pagoString != null) {
                    if (pagoString.equals("Pagar_Tarjeta")) {
                        pago = new Pagar_Tarjeta();
                    }
                    else {
                        pago = new Pagar_Efectivo();
                    }
                }


                // Recuperar cliente
                Cliente cliente = clienteDao.findClienteById(clienteId);

                // Crear pedido
                Pedido pedido = new Pedido(id, cliente);
                pedido.setFecha(fecha);
                pedido.setPrecioTotal(precioTotal);
                pedido.setEstado(estado);
                pedido.setMetodoPago(pago);

                pedidos.add(pedido);
            }
        }
        return pedidos;
    }

    @Override
    public List<LineaPedido> findLineasPedidoByPedidoId(int pedidoId) throws SQLException {
        List<LineaPedido> lineasPedido = new ArrayList<>();
        String FIND_LINEA_BY_PEDIDO_ID = "SELECT lineaspedido.id, lineaspedido.cantidad, lineaspedido.producto_id, productos.precio "
                +
                "FROM lineaspedido " +
                "JOIN productos ON lineaspedido.producto_id = productos.id " +
                "WHERE lineaspedido.pedido_id = ?";

        try (Connection conexion = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER,
                DatabaseConf.PASSWORD);
                PreparedStatement pstmt = conexion.prepareStatement(FIND_LINEA_BY_PEDIDO_ID)) {

            pstmt.setInt(1, pedidoId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int cantidad = rs.getInt("cantidad");
                int productoId = rs.getInt("producto_id");
                double precio = rs.getDouble("precio");

                // Recuperar el producto asociado usando ProductoDao
                Producto producto = productoDao.findProductoById(productoId);

                // Crear la línea de pedido
                LineaPedido linea = new LineaPedido(cantidad, producto, precio);
                linea.setId(id);

                lineasPedido.add(linea);
            }
        }
        return lineasPedido;
    }

}

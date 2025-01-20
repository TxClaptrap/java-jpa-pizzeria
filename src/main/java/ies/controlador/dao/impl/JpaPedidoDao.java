package ies.controlador.dao.impl;

import java.sql.SQLException;
import java.util.List;
import ies.controlador.dao.PedidoDao;
import ies.modelo.Cliente;
import ies.modelo.EstadoPedido;
import ies.modelo.LineaPedido;
import ies.modelo.Pedido;
import ies.modelo.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaPedidoDao implements PedidoDao {
    private final EntityManagerFactory entityManagerFactory;

    public JpaPedidoDao() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    @Override
    public void insertPedido(Pedido pedido) throws SQLException {
        EntityManager entityManager = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(pedido);

            // Persistir las líneas de pedido sin el producto
            for (LineaPedido lineaPedido : pedido.getListaLineaPedidos()) {
                lineaPedido.setProducto(null); // Eliminar referencia al producto
                lineaPedido.setPedido(pedido); // relación inversa
                entityManager.persist(lineaPedido);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error al insertar el pedido: " + e.getMessage(), e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public void updatePedido(Pedido pedido) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(pedido);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void deletePedido(Pedido pedido) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Pedido pedidoExistente = entityManager.find(Pedido.class, pedido.getId());
            if (pedidoExistente != null) {
                entityManager.remove(pedidoExistente);
            } else {
                throw new SQLException("Pedido no encontrado con ID: " + pedido.getId());
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction();
            }
            throw new SQLException("Error al eliminar el pedido: " + e.getMessage(), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Pedido findPedidoById(int idPedido) throws SQLException {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            Pedido pedido = entityManager.find(Pedido.class, idPedido);
            if (pedido == null) {
                throw new SQLException("No se encontró el pedido con ID: " + idPedido);
            }
            pedido.getListaLineaPedidos().size();
            return pedido;
        } catch (Exception e) {
            throw new SQLException("Error al buscar el pedido con ID: " + idPedido, e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Pedido> findPedidosByClienteId(int clienteId) throws SQLException {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            String consulta = "SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId";
            List<Pedido> pedidos = entityManager
                    .createQuery(consulta, Pedido.class)
                    .setParameter("clienteId", clienteId)
                    .getResultList();
            for (Pedido pedido : pedidos) {
                pedido.getListaLineaPedidos().size();
            }
            return pedidos;
        } catch (Exception e) {
            throw new SQLException("Error al buscar los pedidos del cliente con ID: " + clienteId, e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Pedido> findPedidosByEstado(EstadoPedido estado) throws SQLException {
        EntityManager entityManager = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            String consulta = "SELECT p FROM Pedido p WHERE p.estado = :estado";
            List<Pedido> pedidos = entityManager
                    .createQuery(consulta, Pedido.class)
                    .setParameter("estado", estado)
                    .getResultList();
            for (Pedido pedido : pedidos) {
                pedido.getListaLineaPedidos().size();
            }
            return pedidos;
        } catch (Exception e) {
            throw new SQLException("Error al buscar los pedidos con estado: " + estado, e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<LineaPedido> findLineasPedidoByPedidoId(int pedidoId) throws SQLException {
        EntityManager entityManager = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();

            String consulta = "SELECT lp FROM LineaPedido lp WHERE lp.pedido.id = :pedidoId";
            List<LineaPedido> lineasPedido = entityManager
                    .createQuery(consulta, LineaPedido.class)
                    .setParameter("pedidoId", pedidoId)
                    .getResultList();

            return lineasPedido;
        } catch (Exception e) {
            throw new SQLException("Error al buscar las líneas de pedido con Pedido ID: " + pedidoId, e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    @Override
    public void addLineaPedido(Producto producto, int cantidad, int clienteId)
            throws IllegalAccessException, SQLException {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }

        EntityManager entityManager = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Cliente cliente = entityManager.find(Cliente.class, clienteId);
            if (cliente == null) {
                throw new IllegalAccessException("El cliente con ID " + clienteId + " no existe.");
            }
            
            //pedido en estado PENDIENTE
            Pedido pedidoPendiente = entityManager.createQuery(
                    "SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.estado = :estado", Pedido.class)
                    .setParameter("clienteId", clienteId)
                    .setParameter("estado", EstadoPedido.PENDIENTE)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (pedidoPendiente == null) {
                pedidoPendiente = new Pedido(cliente);
                entityManager.persist(pedidoPendiente);
            }
            LineaPedido nuevaLineaPedido = new LineaPedido(cantidad, producto);
            nuevaLineaPedido.setPedido(pedidoPendiente);
            pedidoPendiente.getListaLineaPedidos().add(nuevaLineaPedido);
            double nuevoPrecioTotal = pedidoPendiente.getListaLineaPedidos()
                    .stream()
                    .mapToDouble(LineaPedido::getPrecio)
                    .sum();
            pedidoPendiente.setPrecioTotal(nuevoPrecioTotal);
            entityManager.persist(nuevaLineaPedido);
            entityManager.merge(pedidoPendiente);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error al añadir la línea de pedido: " + e.getMessage(), e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

}

package ies.controlador.dao.impl;

import java.sql.SQLException;
import java.util.List;

import ies.controlador.dao.PedidoDao;
import ies.modelo.EstadoPedido;
import ies.modelo.LineaPedido;
import ies.modelo.Pedido;
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

            for (LineaPedido lineaPedido : pedido.getListaLineaPedidos()) {
                entityManager.persist(lineaPedido);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error al insertar el pedido: " + e.getMessage(), e);
        }
        entityManager.close();
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePedido'");
    }

    @Override
    public Pedido findPedidoById(int idPedido) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findPedidoById'");
    }

    @Override
    public List<Pedido> findPedidosByClienteId(int ClienteId) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findPedidosByClienteId'");
    }

    @Override
    public List<Pedido> findPedidosByEstado(EstadoPedido estado) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findPedidosByEstado'");
    }

    @Override
    public List<LineaPedido> findLineasPedidoByPedidoId(int PedidpId) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findLineasPedidoByPedidoId'");
    }

}

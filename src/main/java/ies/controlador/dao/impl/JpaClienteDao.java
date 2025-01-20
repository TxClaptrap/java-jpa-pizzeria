package ies.controlador.dao.impl;

import java.sql.SQLException;
import java.util.List;

import ies.controlador.dao.ClienteDao;
import ies.modelo.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

public class JpaClienteDao implements ClienteDao {
    private final EntityManagerFactory entityManagerFactory;

    public JpaClienteDao() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    @Override
    public void insertCliente(Cliente cliente) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(cliente); // INSERT
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void updateCliente(Cliente cliente) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin(); // Iniciar la transacción
        entityManager.merge(cliente); // Merge
        entityManager.getTransaction().commit(); // Confirmar la transacción
        entityManager.close(); // Cerrar el EntityManager
    }

    // Hay que hacerlo diferente al concesionario por el lazy
    @Override
    public void deleteCliente(Cliente cliente) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            // Buscar el cliente en la base de datos
            Cliente clienteExistente = entityManager.find(Cliente.class, cliente.getId());

            if (clienteExistente != null) {
                entityManager.remove(clienteExistente);
            } else {
                throw new SQLException("Cliente no encontrado con ID: " + cliente.getId());
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction(); // rollback en caso de error
            }
            throw new SQLException("Error al eliminar el cliente: " + e.getMessage(), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Cliente findClienteByEmail(String email) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Cliente cliente = null;
        try {
            entityManager.getTransaction().begin();
            cliente = entityManager.createQuery("SELECT c FROM Cliente c WHERE c.email = :email", Cliente.class)
                    .setParameter("email", email)
                    .getSingleResult();

            entityManager.getTransaction().commit();
        } catch (NoResultException e) {
            cliente = null;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error al buscar el cliente por email: " + e.getMessage(), e);
        } finally {
            entityManager.close();
        }

        return cliente;
    }

    @Override
    public Cliente findClienteById(int id) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Cliente cliente = entityManager.find(Cliente.class, id);
        
        // Fuerzo la inicialización de la lista para que no de error después de cerrar el entityManager
        if (cliente != null) {
            cliente.getListaPedidos().size();
        }
        entityManager.close();
        return cliente;
    }

    @Override
    public List<Cliente> findAllClientes() throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Cliente> clientes = null;
        try {
            entityManager.getTransaction().begin();
            clientes = entityManager.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error al obtener la lista de clientes: " + e.getMessage(), e);
        } finally {
            entityManager.close();
        }
        return clientes;
    }

}

package ies.controlador.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Hibernate;

import ies.controlador.dao.ClienteDao;
import ies.modelo.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaClienteDao implements ClienteDao {
    private final EntityManagerFactory entityManagerFactory;

    public JpaClienteDao() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    @Override
    public void insertCliente(Cliente cliente) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(cliente); // INSERT
        entityTransaction.commit();
    }

    @Override
    public void updateCliente(Cliente cliente) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.merge(cliente);
    }

    @Override
    public void deleteCliente(Cliente cliente) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.remove(cliente);
        entityTransaction.commit();
    }

    @Override
    public Cliente findClienteByEmail(String email) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Cliente cliente = null;
        try {
            List<Cliente> clientes = entityManager.createQuery(
                    "SELECT c FROM clientes c WHERE c.email = :email", Cliente.class)
                    .setParameter("email", email)
                    .getResultList();
            
            if (!clientes.isEmpty()) {
                cliente = clientes.get(0);
            }
        } finally {
            entityManager.close(); 
        }
        return cliente; 
    }
    

    @Override
    public Cliente findClienteById(int id) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Cliente cliente = entityManager.find(Cliente.class, id);
        entityManager.close();
        return cliente;
    }

    @Override
    public List<Cliente> findAllClientes() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllClientes'");
    }

}

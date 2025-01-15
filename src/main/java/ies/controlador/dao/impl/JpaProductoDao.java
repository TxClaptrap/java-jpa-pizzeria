package ies.controlador.dao.impl;

import java.sql.SQLException;
import java.util.List;

import ies.controlador.dao.ProductoDao;
import ies.modelo.Bebida;
import ies.modelo.Ingrediente;
import ies.modelo.Pasta;
import ies.modelo.Pizza;
import ies.modelo.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

public class JpaProductoDao implements ProductoDao {

    private final EntityManagerFactory entityManagerFactory;

    public JpaProductoDao() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    public void insertProducto(Producto producto) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.merge(producto);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public Ingrediente findIngredienteByName(String nombre) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.createQuery("SELECT i FROM Ingrediente i WHERE i.nombre = :nombre", Ingrediente.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // No se encontró el ingrediente
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void updateProducto(Producto producto) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProducto'");
    }

    @Override
    public void deleteProducto(Producto producto) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProducto'");
    }

    @Override
    public Producto findProductoById(int idProducto) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findProductoById'");
    }

    @Override
    public List<Producto> findAllProductos() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllProductos'");
    }

    @Override
    public List<Ingrediente> findIngredientesByProducto(int idProducto) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findIngredientesByProducto'");
    }

    @Override
    public List<String> findAlergenosByIngrediente(int idIngrediente) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAlergenosByIngrediente'");
    }

}

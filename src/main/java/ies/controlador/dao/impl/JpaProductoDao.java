package ies.controlador.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import ies.controlador.dao.ProductoDao;
import ies.modelo.Ingrediente;
import ies.modelo.Pasta;
import ies.modelo.Pizza;
import ies.modelo.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaProductoDao implements ProductoDao {

    private final EntityManagerFactory entityManagerFactory;

    public JpaProductoDao() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    public void insertProducto(Producto producto) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        List<Ingrediente> ingredientes = new ArrayList<>();
        if (producto instanceof Pizza) {
            ingredientes = ((Pizza) producto).getListaIngredientes();
        } else if (producto instanceof Pasta) {
            ingredientes = ((Pasta) producto).getListaIngredientes();
        }
        List<Ingrediente> ingredientesConID = new ArrayList<>();
        for (Ingrediente i : ingredientes) {
            Ingrediente ingredienteConId = entityManager.find(Ingrediente.class, i.getId());
            if (ingredienteConId == null) {
                entityManager.persist(i);
                ingredienteConId = i;
            }
            ingredienteConId.setAlergenos(i.getAlergenos());
            ingredientesConID.add(ingredienteConId);
        }
        if (producto instanceof Pizza) {
            ((Pizza) producto).setListaIngredientes(ingredientesConID);
        } else if (producto instanceof Pasta) {
            ((Pasta) producto).setListaIngredientes(ingredientesConID);
        }
        entityManager.persist(producto);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /* public Ingrediente findIngredienteByName(String nombre) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.createQuery("SELECT i FROM Ingrediente i WHERE i.nombre = :nombre", Ingrediente.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            entityManager.close();
        }
    } */

    @Override
    public void updateProducto(Producto producto) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin(); // Iniciar la transacción
        entityManager.merge(producto); // Merge
        entityManager.getTransaction().commit(); // Confirmar la transacción
        entityManager.close(); // Cerrar el EntityManager
    }

    @Override
    public void deleteProducto(Producto producto) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Producto productoExistente = entityManager.find(Producto.class, producto.getId());

            if (productoExistente != null) {
                entityManager.remove(productoExistente);
            } else {
                throw new SQLException("Producto no encontrado con ID: " + producto.getId());
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction(); // rollback en caso de error
            }
            throw new SQLException("Error al eliminar el producto: " + e.getMessage(), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Producto findProductoById(int idProducto) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Producto producto = entityManager.find(Producto.class, idProducto);
        try {
            if (producto != null) {
                if (producto instanceof Pizza) {
                    Hibernate.initialize(((Pizza) producto).getListaIngredientes());
                } else if (producto instanceof Pasta) {
                    Hibernate.initialize(((Pasta) producto).getListaIngredientes());
                }
                return producto;
            } else {
                return null;
            }
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Producto> findAllProductos() throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Producto> productos = new ArrayList<>();

        try {
            entityManager.getTransaction().begin();
            productos = entityManager.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();
            for (Producto producto : productos) {
                if (producto instanceof Pizza) {
                    Hibernate.initialize(((Pizza) producto).getListaIngredientes());
                } else if (producto instanceof Pasta) {
                    Hibernate.initialize(((Pasta) producto).getListaIngredientes());
                }
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error al obtener todos los productos: " + e.getMessage(), e);
        }

        entityManager.close();

        return productos;
    }

    @Override
    public List<Ingrediente> findIngredientesByProducto(int idProducto) throws SQLException {
        Producto producto = findProductoById(idProducto);
        if (producto == null) {
            throw new SQLException("Producto no encontrado con ID: " + idProducto);
        }
        if (producto instanceof Pizza) {
            return ((Pizza) producto).getListaIngredientes();
        } else if (producto instanceof Pasta) {
            return ((Pasta) producto).getListaIngredientes();
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> findAlergenosByIngrediente(int idIngrediente) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<String> alergenos = new ArrayList<>();

        try {
            entityManager.getTransaction().begin();
            Ingrediente ingrediente = entityManager.find(Ingrediente.class, idIngrediente);
            if (ingrediente == null) {
                throw new SQLException("Ingrediente no encontrado con ID: " + idIngrediente);
            }
            alergenos = ingrediente.getAlergenos();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new SQLException("Error al obtener los alérgenos del ingrediente: " + e.getMessage(), e);
        }
        entityManager.close();

        return alergenos;
    }

}

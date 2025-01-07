package ies;

import ies.controlador.dao.impl.JdbcProductoDao;
import ies.modelo.*;
import ies.utils.DatabaseConf;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JdbcProductoDaoTest {

    private static JdbcProductoDao productoDao;
    private static Connection connection;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        productoDao = new JdbcProductoDao();
        connection = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
    }

    @BeforeEach
    void cleanDatabase() throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM producto_ingrediente; DELETE FROM productos; DELETE FROM ingredientes; DELETE FROM alergenos;")) {
            stmt.executeUpdate();
        }
    }

    @Test
    @Order(1)
    void testInsertProductoPizza() throws SQLException {
        List<Ingrediente> ingredientes = Arrays.asList(
                new Ingrediente("Mozzarella", Arrays.asList("Lactosa")),
                new Ingrediente("Tomate", Arrays.asList())
        );
        Producto pizza = new Pizza(0, "Pizza Margarita", 8.5, SIZE.GRANDE, ingredientes);

        productoDao.insertProducto(pizza);

        Producto result = productoDao.findProductoById(pizza.getId());
        assertNotNull(result);
        assertEquals("Pizza Margarita", result.getNombre());
        assertEquals(8.5, result.getPrecio());
        assertTrue(result instanceof Pizza);
        assertEquals(2, ((Pizza) result).getListaIngredientes().size());
    }

    @Test
    @Order(2)
    void testUpdateProducto() throws SQLException {
        Producto bebida = new Bebida("Coca-Cola", 1.5, SIZE.MEDIANO);
        productoDao.insertProducto(bebida);

        bebida.setNombre("Coca-Cola Zero");
        bebida.setPrecio(1.7);
        productoDao.updateProducto(bebida);

        Producto result = productoDao.findProductoById(bebida.getId());
        assertNotNull(result);
        assertEquals("Coca-Cola Zero", result.getNombre());
        assertEquals(1.7, result.getPrecio());
    }

    @Test
    @Order(3)
    void testDeleteProducto() throws SQLException {
        Producto pasta = new Pasta("Lasaña", 5.0, Arrays.asList());
        productoDao.insertProducto(pasta);

        productoDao.deleteProducto(pasta);

        Producto result = productoDao.findProductoById(pasta.getId());
        assertNull(result);
    }

    @Test
    @Order(4)
    void testFindAllProductos() throws SQLException {
        Producto pizza = new Pizza("Pizza 4 Quesos", 10.0, SIZE.GRANDE, Arrays.asList());
        Producto bebida = new Bebida( "Agua", 1.0, SIZE.PEQUENO);

        productoDao.insertProducto(pizza);
        productoDao.insertProducto(bebida);

        List<Producto> productos = productoDao.findAllProductos();

        assertNotNull(productos);
        assertEquals(2, productos.size());
    }

    @AfterAll
    static void closeDatabase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

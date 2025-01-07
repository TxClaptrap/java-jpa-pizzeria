package ies;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import ies.controlador.ControladorProducto;
import ies.modelo.Bebida;
import ies.modelo.Pasta;
import ies.modelo.Pizza;
import ies.modelo.Producto;
import ies.modelo.SIZE;
import ies.utils.DatabaseConf;

public class ControladorProductoTest {
    ControladorProducto controladorProducto = new ControladorProducto();

    @BeforeEach
    public void setUp() throws SQLException {
        DatabaseConf.dropAndCreateTables(); // Limpia y reinicia las tablas.
        controladorProducto = new ControladorProducto();
    }

    @Test
    public void testRegistrarProducto() throws SQLException {
        Producto pizza = new Pizza("Pizza Margarita", 8.99, SIZE.MEDIANO, new ArrayList<>());
        controladorProducto.registrarProducto(pizza);

        Producto productoEncontrado = controladorProducto.encontrarProductoById(pizza.getId());

        assertNotNull(productoEncontrado);
        assertEquals(pizza.getNombre(), productoEncontrado.getNombre());
        assertEquals(pizza.getPrecio(), productoEncontrado.getPrecio());
    }

    @Test
    public void testActualizarProducto() throws SQLException {
        Producto bebida = new Bebida("Coca Cola", 2.50, SIZE.PEQUENO);
        controladorProducto.registrarProducto(bebida);

        bebida.setPrecio(3.00);
        bebida.setNombre("Coca Cola Zero");
        controladorProducto.actualizarProducto(bebida);

        Producto productoActualizado = controladorProducto.encontrarProductoById(bebida.getId());

        assertNotNull(productoActualizado);
        assertEquals(3.00, productoActualizado.getPrecio());
        assertEquals("Coca Cola Zero", productoActualizado.getNombre());
    }

    @Test
    public void testBorrarProducto() throws SQLException {
        Producto pasta = new Pasta("Espagueti Carbonara", 7.50, new ArrayList<>());
        controladorProducto.registrarProducto(pasta);

        controladorProducto.borrarProducto(pasta);

        Producto productoBorrado = controladorProducto.encontrarProductoById(pasta.getId());

        assertNull(productoBorrado);
    }

    @Test
    public void testEncontrarProductoById() throws SQLException {
        Producto pizza = new Pizza("Pizza Cuatro Quesos", 10.99, SIZE.GRANDE, new ArrayList<>());
        controladorProducto.registrarProducto(pizza);

        Producto productoEncontrado = controladorProducto.encontrarProductoById(pizza.getId());

        assertNotNull(productoEncontrado);
        assertEquals(pizza.getId(), productoEncontrado.getId());
    }

    @Test
    public void testEncontrarTodosLosProductos() throws SQLException {
        Producto pizza = new Pizza("Pizza Margarita", 8.99, SIZE.MEDIANO, new ArrayList<>());
        Producto bebida = new Bebida("Coca Cola", 2.50, SIZE.PEQUENO);
        Producto pasta = new Pasta("Espagueti Carbonara", 7.50, new ArrayList<>());

        controladorProducto.registrarProducto(pizza);
        controladorProducto.registrarProducto(bebida);
        controladorProducto.registrarProducto(pasta);

        List<Producto> productos = controladorProducto.encontrarAllProductos();

        assertEquals(3, productos.size());
        assertTrue(productos.stream().anyMatch(p -> p instanceof Pizza && p.getNombre().equals("Pizza Margarita")));
        assertTrue(productos.stream().anyMatch(p -> p instanceof Bebida && p.getNombre().equals("Coca Cola")));
        assertTrue(productos.stream().anyMatch(p -> p instanceof Pasta && p.getNombre().equals("Espagueti Carbonara")));
    }

}

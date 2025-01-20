package ies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.List;
import ies.controlador.ControladorCliente;
import ies.modelo.Cliente;


import static org.junit.jupiter.api.Assertions.*;

class ControladorClienteTest {

    private ControladorCliente controladorCliente;

    @BeforeEach
    public void setUp() throws SQLException {
        controladorCliente = new ControladorCliente();
    }

    @Test
    void testRegistrarCliente() throws SQLException {
        Cliente cliente = new Cliente("11111111Q", "Pepe", "Pepote", "600000000", "email@test.com", "password");
        controladorCliente.registrarCliente(cliente);
        assertNotNull(cliente.getId());
        assertEquals("email@test.com", cliente.getEmail());
    }

    @Test
    void testRegistrarClienteExistente() throws SQLException {
        Cliente cliente = new Cliente("11111111Q", "Pepe", "Pepote", "600000000", "email@test.com", "password");
        controladorCliente.registrarCliente(cliente);

        Cliente clienteDuplicado = new Cliente("11111112R", "Juan", "Perez", "700000000", "email@test.com", "password2");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controladorCliente.registrarCliente(clienteDuplicado);
        });
        assertEquals("Ya hay un usuario registrado con ese email.", exception.getMessage());
    }

    @Test
    void testLoginClienteExitoso() throws SQLException {
        Cliente cliente = new Cliente("11111111Q", "Pepe", "Pepote", "600000000", "email@test.com", "password");
        controladorCliente.registrarCliente(cliente);
        Cliente loggedInCliente = controladorCliente.loginCliente("email@test.com", "password");
        assertNotNull(loggedInCliente);
        assertEquals(cliente.getId(), loggedInCliente.getId());
    }

    @Test
    void testLoginClienteFallido() throws SQLException {
        Cliente cliente = new Cliente("11111111Q", "Pepe", "Pepote", "600000000", "email@test.com", "password");
        controladorCliente.registrarCliente(cliente);
        SQLException exception = assertThrows(SQLException.class, () -> {
            controladorCliente.loginCliente("email@test.com", "wrongpassword");
        });
        assertEquals("Contraseña incorrecta para el cliente con email: email@test.com", exception.getMessage());
    }

    @Test
    void testActualizarCliente() throws SQLException {
        Cliente cliente = new Cliente("11111111Q", "Pepe", "Pepote", "600000000", "email@test.com", "password");
        controladorCliente.registrarCliente(cliente);
        cliente.setTelefono("611111111");
        controladorCliente.actualizarCliente(cliente);
        Cliente updatedCliente = controladorCliente.loginCliente("email@test.com", "password");
        assertEquals("611111111", updatedCliente.getTelefono());
    }

    @Test
    void testBorrarCliente() throws SQLException {
        Cliente cliente = new Cliente("11111111Q", "Pepe", "Pepote", "600000000", "email@test.com", "password");
        controladorCliente.registrarCliente(cliente);
        controladorCliente.borrarCliente(cliente);
        cliente = controladorCliente.encontrarById(cliente.getId());
        assertNull(cliente);
    }

    @Test
    void testEncontrarTodos() throws SQLException {
        Cliente cliente1 = new Cliente("11111111Q", "Pepe", "Pepote", "600000000", "email1@test.com", "password");
        Cliente cliente2 = new Cliente("11111112R", "Juan", "Perez", "700000000", "email2@test.com", "password");
        controladorCliente.registrarCliente(cliente1);
        controladorCliente.registrarCliente(cliente2);
        List<Cliente> clientes = controladorCliente.encontrarTodos();
        assertEquals(2, clientes.size());
    }

    @Test
    void testEncontrarClientePorEmail() throws SQLException {
        Cliente cliente = new Cliente("11111111Q", "Pepe", "Pepote", "600000000", "email@test.com", "password");
        controladorCliente.registrarCliente(cliente);
        Cliente foundCliente = controladorCliente.encontrarClienteByEmail("email@test.com");
        assertNotNull(foundCliente);
        assertEquals(cliente.getEmail(), foundCliente.getEmail());
    }
}

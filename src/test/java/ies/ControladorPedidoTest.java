package ies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.List;

import ies.controlador.ControladorCliente;
import ies.controlador.ControladorPedido;
import ies.controlador.ControladorProducto;
import ies.modelo.Cliente;
import ies.modelo.Pedido;
import ies.modelo.Pizza;
import ies.modelo.LineaPedido;
import ies.modelo.Producto;
import ies.modelo.SIZE;
import ies.modelo.Pagable;
import ies.modelo.Pagar_Tarjeta;
import ies.modelo.EstadoPedido;
import ies.modelo.Ingrediente;
import static org.junit.jupiter.api.Assertions.*;

class ControladorPedidoTest {
    private ControladorPedido controladorPedido;
    private ControladorCliente controladorCliente;
    ControladorProducto controladorProducto;

    @BeforeEach
    public void setUp() throws SQLException {
        controladorPedido = new ControladorPedido();
        controladorCliente = new ControladorCliente();
        controladorProducto = new ControladorProducto();
    }

    @Test
    void testRegistrarPedido() throws SQLException {
        Cliente cliente = new Cliente("11111111Q", "Pepe", "Pepote", "600000000", "email@test.com", "password");
        controladorCliente.registrarCliente(cliente);
        Pedido pedido = new Pedido(cliente);
        controladorPedido.registrarPedido(pedido);
        Pedido pedidoEncontrado = controladorPedido.encontrarPedidoById(pedido.getId());
        assertNotNull(pedidoEncontrado);
        assertEquals(cliente.getId(), pedidoEncontrado.getCliente().getId());
    }

    @Test
    void testActualizarPedido() throws SQLException {
        Cliente cliente = new Cliente("11111111Q", "Pepe", "Pepote", "600000000", "email@test.com", "password");
        controladorCliente.registrarCliente(cliente);
        Pedido pedido = new Pedido(cliente);
        controladorPedido.registrarPedido(pedido);
        pedido.setEstado(EstadoPedido.FINALIZADO);
        controladorPedido.actualizarPedido(pedido);
        Pedido pedidoActualizado = controladorPedido.encontrarPedidoById(pedido.getId());
        assertEquals(EstadoPedido.FINALIZADO, pedidoActualizado.getEstado());
    }

    @Test
    void testEncontrarPedidosPorCliente() throws SQLException {
        Cliente cliente = new Cliente("11111111Q", "Pepe", "Pepote", "600000000", "email@test.com", "password");
        controladorCliente.registrarCliente(cliente);
        Pedido pedido = new Pedido(cliente);
        Pedido pedido2 = new Pedido(cliente);
        controladorPedido.registrarPedido(pedido);
        controladorPedido.registrarPedido(pedido2);

        // Verificamos que se encuentren todos los pedidos del cliente
        List<Pedido> pedidos = controladorPedido.encontrarPedidosByClienteId(cliente.getId());
        assertEquals(2, pedidos.size());
    }

    @Test
    void testCancelarPedido() throws SQLException, IllegalAccessException {
        Cliente cliente = new Cliente("11111111Q", "Pepe", "Pepote", "600000000", "email@test.com", "password");
        controladorCliente.registrarCliente(cliente);
        Pedido pedido = new Pedido(cliente);
        controladorPedido.registrarPedido(pedido);
        controladorPedido.cancelarPedido(cliente);
        Pedido pedidoCancelado = controladorPedido.encontrarPedidoById(pedido.getId());
        assertEquals(EstadoPedido.CANCELADO, pedidoCancelado.getEstado());
    }

    @Test
    void testFinalizarPedido() throws SQLException, IllegalAccessException {
        Cliente cliente = new Cliente("11111111Q", "Pepe", "Pepote", "600000000", "email@test.com", "password");
        controladorCliente.registrarCliente(cliente);
        Pedido pedido = new Pedido(cliente);
        controladorPedido.registrarPedido(pedido);

        Pagable tipoPago = new Pagar_Tarjeta();
        controladorPedido.finalizarPedido(cliente, tipoPago);
        Pedido pedidoFinalizado = controladorPedido.encontrarPedidoById(pedido.getId());
        assertEquals(EstadoPedido.FINALIZADO, pedidoFinalizado.getEstado());
    }

    @Test
    void testEntregarPedido() throws SQLException, IllegalAccessException {
        Cliente cliente = new Cliente("11111111Q", "Pepe", "Pepote", "600000000", "email@test.com", "password");
        controladorCliente.registrarCliente(cliente);
        Pedido pedido = new Pedido(cliente);
        pedido.setEstado(EstadoPedido.FINALIZADO);
        controladorPedido.registrarPedido(pedido);
        controladorPedido.entregarPedido(cliente);
        Pedido pedidoEntregado = controladorPedido.encontrarPedidoById(pedido.getId());
        assertEquals(EstadoPedido.ENTREGADO, pedidoEntregado.getEstado());
    }

    @Test
    void testAgregarLineaPedido() throws SQLException, IllegalAccessException {
        Cliente cliente = new Cliente("11111111Q", "Pepe", "Pepote", "600000000", "email@test.com", "password");
        controladorCliente.registrarCliente(cliente);
        Ingrediente echamas = new Ingrediente("Queso", List.of("lactosa", "adictivos"));
        Ingrediente baseTrigo = new Ingrediente("Base con trigo", List.of("gluten", "sulfitos"));
        Ingrediente tomatico = new Ingrediente("Tomate", List.of("fructosa"));
        Ingrediente chicha = new Ingrediente("Jamón York", List.of());
        Ingrediente gazpacho = new Ingrediente("Piña", List.of("fructosa"));
        Producto aberrante = new Pizza("Hawaiana", 8, SIZE.MEDIANO,
                List.of(baseTrigo, tomatico, echamas, chicha, gazpacho));
        controladorProducto.registrarProducto(aberrante);
        Pedido pedido = new Pedido(cliente);
        controladorPedido.registrarPedido(pedido);
        controladorPedido.agregarLineaPedido(aberrante, 2, cliente);
        List<LineaPedido> lineas = controladorPedido.encontrarLineasPedidoByPedidoId(pedido.getId());
        assertEquals(1, lineas.size());
    }
}

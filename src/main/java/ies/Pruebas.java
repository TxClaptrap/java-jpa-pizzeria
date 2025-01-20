package ies;

import java.sql.SQLException;
import java.util.List;
import ies.controlador.ControladorCliente;
import ies.controlador.ControladorPedido;
import ies.controlador.ControladorProducto;
import ies.modelo.Bebida;
import ies.modelo.Cliente;
import ies.modelo.EstadoPedido;
import ies.modelo.Ingrediente;
import ies.modelo.Pagar_Tarjeta;
import ies.modelo.Pasta;
import ies.modelo.Pedido;
import ies.modelo.Pizza;
import ies.modelo.Producto;
import ies.modelo.SIZE;

public class Pruebas {
    public static void main(String[] args) {

        ControladorPedido controladorPedido = new ControladorPedido();
        ControladorCliente controladorCliente = new ControladorCliente();
        ControladorProducto controladorProducto = new ControladorProducto();
        try {
            Cliente cliente = new Cliente("11111111P", "Pepe", "C/Falsa", "666000000", "zzz@gmail.com", "cosa");
            Cliente cliente2 = new Cliente("21111111P", "Pepi", "C/Falsa", "766000000", "zxzz@gmail.com", "coso");
            Cliente clienteActual;

            Ingrediente echamas = new Ingrediente("Queso", List.of("lactosa", "adictivos"));
            Ingrediente baseTrigo = new Ingrediente("Base con trigo", List.of("gluten", "sulfitos"));
            Ingrediente baseSosa = new Ingrediente("Base sin trigo", List.of());
            Ingrediente tomatico = new Ingrediente("Tomate", List.of("fructosa"));
            Ingrediente chicha = new Ingrediente("Jamón York", List.of());
            Ingrediente gazpacho = new Ingrediente("Piña", List.of("fructosa"));
            Ingrediente chicha2 = new Ingrediente("Carne picada", List.of());

            Producto aberrante = new Pizza("Hawaiana", 8, SIZE.MEDIANO,
                    List.of(baseTrigo, tomatico, echamas, chicha, gazpacho));
            Producto bolognese = new Pasta("Boloñesa celiaca", 10, List.of(echamas, baseSosa, tomatico, chicha2));
            Producto prueba = new Pasta("Bolo", 10, List.of(baseSosa, tomatico, chicha2));
            Producto cola = new Bebida("CocaCola", 2, SIZE.PEQUENO);
            Producto carbonara = new Pizza("Carbonara", 10, SIZE.GRANDE, List.of(echamas, baseTrigo, chicha));
            Pedido pedido = new Pedido(cliente);
            Pedido pedido2 = new Pedido(cliente);
            Pagar_Tarjeta pagoTarjeta = new Pagar_Tarjeta();

            controladorCliente.registrarCliente(cliente);
            controladorCliente.registrarCliente(cliente2);

            System.out.println(
                    "***********************" + controladorCliente.encontrarClienteByEmail(cliente.getEmail()));
            cliente.setPassword("1234");

            // System.out.println(cliente);
            controladorCliente.actualizarCliente(cliente);

            System.out.println(controladorCliente.encontrarById(1));

            System.out.println(controladorCliente.encontrarTodos());
            // controladorCliente.borrarCliente(cliente);

            controladorProducto.registrarProducto(carbonara);
            controladorProducto.registrarProducto(bolognese);
            controladorProducto.registrarProducto(cola);
            controladorProducto.registrarProducto(aberrante);

            carbonara.setPrecio(20);

            controladorProducto.actualizarProducto(carbonara);
            controladorProducto.borrarProducto(aberrante);
            aberrante.setId(0);

            controladorProducto.registrarProducto(aberrante);
            System.out.println("***********por id**************");
            System.out.println(controladorProducto.encontrarProductoById(1));
            System.out.println("************findAll***********");
            System.out.println(controladorProducto.encontrarAllProductos());
            System.out.println("***********ingredienteByProducto********");
            System.out.println(controladorProducto.encontrarIngredientesByProducto(1));
            System.out.println("***********alergenoByIngrediente********");
            System.out.println(controladorProducto.encontrarAlergenosByIngrediente(2));

            cliente.setId(1);
            System.out.println(cliente.getId());

            clienteActual = controladorCliente.loginCliente(cliente.getEmail(), "1234");
            // System.out.println(clienteActual);

            controladorPedido.registrarPedido(pedido);
            // controladorPedido.registrarPedido(pedido2);
            // controladorPedido.borrarPedido(pedido);

            System.out.println(controladorPedido.encontrarPedidoById(1));
            System.out.println("*************************************");
            System.out.println(controladorPedido.encontrarPedidosByClienteId(1));
            System.out.println("*****************AAAAAAAAAAAAAAAAAAAAAAA***************");
            System.out.println(controladorPedido.encontrarPedidosByEstado(EstadoPedido.PENDIENTE));
            try {
                controladorPedido.agregarLineaPedido(carbonara, 2, clienteActual);
                controladorPedido.finalizarPedido(clienteActual, pagoTarjeta);
                controladorPedido.entregarPedido(clienteActual);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}

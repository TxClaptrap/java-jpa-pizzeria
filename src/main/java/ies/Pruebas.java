package ies;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import ies.controlador.ClientesWrapper;
import ies.controlador.ControladorCliente;
import ies.controlador.ControladorProducto;
import ies.controlador.GestorFicheros;
import ies.controlador.PizzasWrapper;
import ies.controlador.Varios;
import ies.modelo.Bebida;
import ies.modelo.Cliente;
import ies.modelo.Ingrediente;
import ies.modelo.Pasta;
import ies.modelo.Pizza;
import ies.modelo.Producto;
import ies.modelo.ProductosSeparados;
import ies.modelo.SIZE;

public class Pruebas {
    public static void main(String[] args) {

        ControladorCliente controladorCliente = new ControladorCliente();
        ControladorProducto controladorProducto = new ControladorProducto();

        Cliente cliente = new Cliente("11111111P", "Pepe", "C/Falsa", "666000000", "zzz@gmail.com", "cosa");
        Cliente cliente2 = new Cliente("21111111P", "Pepi", "C/Falsa", "766000000", "zxzz@gmail.com", "coso");

        Ingrediente echamas = new Ingrediente("Queso", List.of("lactosa", "adictivos"));
        Ingrediente baseTrigo = new Ingrediente("Base con trigo", List.of("gluten", "sulfitos"));
        Ingrediente baseSosa = new Ingrediente("Base sin trigo", List.of());
        Ingrediente tomatico = new Ingrediente("Tomate", List.of("fructosa"));
        Ingrediente chicha = new Ingrediente("Jamón York", List.of());
        Ingrediente gazpacho = new Ingrediente("Piña", List.of("fructosa"));
        Ingrediente chicha2 = new Ingrediente("Carne picada", List.of());
        
        Producto aberrante = new Pizza("Hawaiana", 8, SIZE.MEDIANO , List.of(baseTrigo, tomatico, echamas, chicha, gazpacho));
        Producto bolognese = new Pasta("Boloñesa celiaca", 10, List.of(echamas, baseSosa, tomatico, chicha2));
        Producto cola = new Bebida("CocaCola", 2, SIZE.PEQUENO);
        Producto carbonara = new Pizza("Carbonara", 10, SIZE.GRANDE, List.of(echamas, baseTrigo, chicha));


        try {
            controladorCliente.registrarCliente(cliente);
            controladorCliente.registrarCliente(cliente2);
            cliente.setPassword("1234");


            //System.out.println(cliente);
            controladorCliente.actualizarCliente(cliente);

            System.out.println(controladorCliente.encontrarById(1));

            System.out.println(controladorCliente.encontrarTodos());
            //controladorCliente.borrarCliente(cliente);

            controladorProducto.registrarProducto(carbonara);
            controladorProducto.registrarProducto(bolognese);
            //controladorProducto.registrarProducto(aberrante);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /*
     * public static void main(String[] args) {
     * ControladorCliente controladorCliente = new ControladorCliente();
     * ControladorProducto controladorProducto = new ControladorProducto();
     * Varios gestorFicheros = new Varios();
     * 
     * // Importar
     * 
     * List<Cliente> administradores;
     * try {
     * administradores = controladorCliente.leerClientes();
     * administradores.forEach(System.out::println);
     * } catch (IOException e) {
     * administradores = new ArrayList<>();
     * e.printStackTrace();
     * }
     * 
     * // Exportar
     * try {
     * gestorFicheros.exportarAdministradoresTXT(administradores);
     * } catch (IOException e) {
     * System.out.println("No se ha exportado.");
     * e.printStackTrace();
     * }
     * 
     * Ingrediente echamas = new Ingrediente(1, "Queso", List.of("lactosa",
     * "adictivos"));
     * Ingrediente baseTrigo = new Ingrediente(2, "Base con trigo",
     * List.of("gluten", "sulfitos"));
     * Ingrediente baseSosa = new Ingrediente(3, "Base sin trigo", List.of());
     * Ingrediente tomatico = new Ingrediente(4, "Tomate", List.of("fructosa"));
     * Ingrediente chicha = new Ingrediente(5, "Jamón York", List.of());
     * Ingrediente gazpacho = new Ingrediente(6, "Piña", List.of("fructosa"));
     * Ingrediente chicha2 = new Ingrediente(7, "Carne picada", List.of());
     * 
     * Pizza aberrante = new Pizza(1, "Hawaiana", 8, SIZE.MEDIANO,
     * List.of(baseTrigo, tomatico, echamas, chicha, gazpacho));
     * Pasta bolognese = new Pasta(2, "Boloñesa celiaca", 10, List.of(echamas,
     * baseSosa, tomatico, chicha2));
     * 
     * Bebida cocacola = new Bebida(3, "Cocacola", 2, SIZE.GRANDE);
     * 
     * ControladorPruebas pruebas = new ControladorPruebas();
     * List<Producto> productos = List.of(aberrante, bolognese, cocacola);
     * 
     * try {
     * ProductosSeparados productosSeparados = pruebas.separarProductos(productos);
     * } catch (Exception e) {
     * System.err.println("No se ha podido añadir");
     * e.printStackTrace();
     * }
     * 
     * // Exportar
     * try {
     * gestorFicheros.exportarAdministradoresTXT(administradores);
     * } catch (IOException e) {
     * System.out.println("No se ha exportado.");
     * e.printStackTrace();
     * }
     * 
     * try {
     * gestorFicheros.exportarPizzaXML(List.of(aberrante, aberrante, aberrante));
     * } catch (JAXBException e) {
     * System.out.println("No se ha exportado.");
     * e.printStackTrace();
     * }
     * List<Pizza> pizzas;
     * try {
     * pizzas = gestorFicheros.importarPizzaXML();
     * pizzas.forEach(System.out::println);
     * 
     * } catch (JAXBException e) {
     * System.out.println("No se ha importar.");
     * e.printStackTrace();
     * }
     * 
     * }
     * 
     * 
     */
}

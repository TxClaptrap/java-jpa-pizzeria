package ies.controlador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import ies.modelo.Bebida;
import ies.modelo.Cliente;
import ies.modelo.Ingrediente;
import ies.modelo.Pasta;
import ies.modelo.Pizza;
import ies.modelo.Producto;
import ies.modelo.ProductosSeparados;

public class Varios {
    private final String rutaTXT = "administradores.txt";
    private final String rutaXML = "pizzas.xml";
    private final String rutaCSV = "ingredientes.csv";

/* 

    // Lector a pelo
    public List<Cliente> leerAdministradores() throws IOException {
        try (Stream<String> lineas = Files.lines(Path.of(rutaTXT))) {
            return lineas.map(linea -> {
                List<String> administradores = List.of(linea.split("[,;|]"));
                administradores = administradores.stream().map(String::trim).toList();

                return new Cliente(Integer.parseInt(administradores.get(0)), administradores.get(1),
                        administradores.get(2), administradores.get(3),
                        administradores.get(4),
                        administradores.get(5), administradores.get(6), true);
            }).toList();
        }
    }

    // Exportar a pelo
    public boolean exportarAdministradoresTXT(List<Cliente> clientes) throws IOException {
        Files.write(Path.of(rutaTXT),
                clientes.stream().map(cliente -> clienteToString(cliente, ",")).toList());
        return true;
    }

    private static String clienteToString(Cliente cliente, String separador) {
        return String.format("%d%s%s%s%s%s%s%s%s%s%s%s%s", cliente.getId(), separador, cliente.getDni(), separador,
                cliente.getNombre(), separador, cliente.getDireccion(), separador, cliente.getTelefono(), separador,
                cliente.getEmail(), separador, cliente.getPassword());
    }

    // Lista de clientes a XML
    public boolean exportarXML(List<Cliente> clientes) throws JAXBException {
        ClientesWrapper wrap = new ClientesWrapper(clientes);
        JAXBContext context = JAXBContext.newInstance(Cliente.class, ClientesWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrap, new File(rutaXML));
        return true;
    }

    // XML a lista de pizzas
    public List<Pizza> importarPizzaXML() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(PizzasWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        PizzasWrapper wrap = (PizzasWrapper) unmarshaller.unmarshal(new File(rutaXML));
        return wrap.getPizzas();
    }

    // Lista de ingredientes a CSV
    public boolean exportarCSV(List<Ingrediente> ingredientes)
            throws FileNotFoundException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        File archivoCSV = new File(rutaCSV);

        try (PrintWriter pw = new PrintWriter(archivoCSV)) {
            StatefulBeanToCsv<Ingrediente> beanToCsv = new StatefulBeanToCsvBuilder<Ingrediente>(pw).withSeparator(';')
                    .build();
            beanToCsv.write(ingredientes);

            return true;
        }
    }

    public List<Ingrediente> importarCSV() throws FileNotFoundException, IOException {
        try (FileReader fr = new FileReader(rutaCSV)) {
            CsvToBean<Ingrediente> csvToBean = new CsvToBeanBuilder<Ingrediente>(fr)
                    .withType(Ingrediente.class)
                    .withSeparator(';') // Especifica el delimitador como punto y coma, si no, no lo pilla, ya que es
                                        // "," por defecto.
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        }
    }

    // Método que separa productos por tipo y devuelve un objeto de ProductosSeparados
    public ProductosSeparados separarProductosEnListas(List<Producto> productos) throws Exception {
        List<Producto> pizzas = new ArrayList<>();
        List<Producto> pastas = new ArrayList<>();
        List<Producto> bebidas = new ArrayList<>();

        for (Producto producto : productos) {
            if (producto instanceof Pizza) {
                pizzas.add(producto);
            } else if (producto instanceof Pasta) {
                pastas.add(producto);
            } else if (producto instanceof Bebida) {
                bebidas.add(producto);
            }
        }

        return new ProductosSeparados(pizzas, pastas, bebidas);
    }

        // Exportar a pelo Pizzas
        public boolean exportarPizzas(List<Pizza> pizzas) throws IOException {
            Files.write(Path.of(rutaTXT),
                    pizzas.stream().map(pizza -> pizzaToString(pizza, ",")).toList());
            return true;
        }
    
        private static String pizzaToString(Pizza pizza, String separador) {
            return String.format("%d%s%s%s%s%s%s%s%s%s%s%s%s", pizza.getId(), separador,
                    pizza.getNombre(), separador, pizza.getPrecio(), separador, pizza.getListaIngredientes());
        }

        //Pizzas a XML
        public boolean exportarPizzaXML(List<Pizza> pizzas) throws JAXBException {
            PizzasWrapper wrap = new PizzasWrapper(pizzas);
            JAXBContext context = JAXBContext.newInstance(Pizza.class, PizzasWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(wrap, new File("pizzas.xml"));
            return true;
        }

*/
}

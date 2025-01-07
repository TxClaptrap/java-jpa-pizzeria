package ies.controlador;

import java.sql.SQLException;
import java.util.List;

import ies.controlador.dao.ClienteDao;
import ies.controlador.dao.impl.JdbcClienteDao;
import ies.modelo.Cliente;

public class ControladorCliente {

    ClienteDao clienteDao = new JdbcClienteDao();

    public void registrarCliente(Cliente cliente) throws SQLException {
        if (clienteDao.findClienteByEmail(cliente.getEmail()) == null) {
            clienteDao.insertCliente(cliente);
        } else {
            throw new IllegalArgumentException("Ya hay un usuario registrado con ese email.");
        }
    }

    // loginCliente(mail, pass)
    public Cliente loginCliente(String email, String password) throws SQLException {
        if (clienteDao.findClienteByEmail(email).getEmail().equals(email)
                && clienteDao.findClienteByEmail(email).getPassword().equals(password)) {
            return clienteDao.findClienteByEmail(email);
        } else {
            System.out.println("No se pudo acceder a la lista de clientes.");
            return null;
        }
    }

    public void actualizarCliente(Cliente cliente) throws SQLException {
        clienteDao.updateCliente(cliente);
    }

    public void borrarCliente(Cliente cliente) throws SQLException {
        clienteDao.deleteCliente(cliente);
    }

    public List<Cliente> encontrarTodos() throws SQLException {
        return clienteDao.findAllClientes();
    }

    public Cliente encontrarById(int idCliente) throws SQLException {
        return clienteDao.findClienteById(idCliente);
    }

    /*
     * Antigua Pizzería
     * 
     * //clienteActual
     * private Cliente clienteActual;
     * private List<Cliente> listaClientes;
     * GestorFicheros gestorFicheros;
     * 
     * public ControladorCliente() {
     * gestorFicheros = new GestorFicheros();
     * }
     * 
     * public ControladorCliente(Cliente clienteActual) {
     * this.clienteActual = clienteActual;
     * this.listaClientes = new ArrayList<Cliente>();
     * gestorFicheros = new GestorFicheros();
     * }
     * 
     * public Cliente getClienteActual() {
     * return clienteActual;
     * }
     * 
     * public void setClienteActual(Cliente clienteActual) {
     * this.clienteActual = clienteActual;
     * }
     * 
     * public List<Cliente> getListaClientes() {
     * return listaClientes;
     * }
     * 
     * public void setListaClientes(List<Cliente> listaClientes) {
     * this.listaClientes = listaClientes;
     * }
     * 
     * //registrarCliente()
     * public void registrarCliente(String dni, String nombre, String direccion,
     * String telefono, String email, String password) {
     * if (listaClientes != null) {
     * boolean emailDuplicado = false;
     * 
     * for (Cliente cliente : listaClientes) {
     * if (cliente.getEmail() != null && cliente.getEmail().equals(email)) {
     * emailDuplicado = true;
     * break;
     * }
     * }
     * 
     * if (!emailDuplicado) {
     * listaClientes.add(new Cliente(dni, nombre, direccion, telefono, email,
     * password));
     * System.out.println("Cliente registrado con éxito.");
     * } else {
     * System.out.println("Error: El email ya está registrado.");
     * }
     * } else {
     * System.out.println("No se pudo acceder a la lista de clientes.");
     * }
     * 
     * }
     * 
     * //loginCliente(mail, pass)
     * public boolean loginCliente(String email, String password) {
     * if (listaClientes != null) {
     * for (Cliente cliente : listaClientes) {
     * if (cliente.getEmail() != null && cliente.getEmail().equals(email) &&
     * cliente.getPassword().equals(password)) {
     * clienteActual = cliente;
     * System.out.println("Login correcto. Bienvenido " + cliente.getNombre());
     * return true;
     * }
     * }
     * System.out.println("Error: Email o contraseña incorrectos.");
     * } else {
     * System.out.println("No se pudo acceder a la lista de clientes.");
     * }
     * return false;
     * }
     * 
     * /*
     * //Métodos de encapsulado, para no hacerlo todo desde GestorFicheros
     * public List<Cliente> leerClientes() throws IOException {
     * return gestorFicheros.leerAdministradores();
     * }
     * 
     * 
     * 
     * public boolean exportarClientesXML(List<Cliente> clientes) throws
     * JAXBException {
     * 
     * return gestorFicheros.exportarXML(clientes);
     * }
     * 
     * public List<Cliente> importarClientesXML() throws JAXBException {
     * return gestorFicheros.importarXML();
     * }
     * 
     *//* */

}

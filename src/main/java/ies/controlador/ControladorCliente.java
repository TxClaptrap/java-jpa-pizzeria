package ies.controlador;

import java.sql.SQLException;
import java.util.List;

import ies.controlador.dao.ClienteDao;
import ies.controlador.dao.impl.JpaClienteDao;
import ies.modelo.Cliente;

public class ControladorCliente {

    ClienteDao clienteDao = new JpaClienteDao();

    public void registrarCliente(Cliente cliente) throws SQLException {
        if (clienteDao.findClienteByEmail(cliente.getEmail()) == null) {
            clienteDao.insertCliente(cliente);
        } else {
            throw new IllegalArgumentException("Ya hay un usuario registrado con ese email.");
        }
    }

    public Cliente loginCliente(String email, String password) throws SQLException {
        Cliente cliente = clienteDao.findClienteByEmail(email);
        if (cliente == null) {
            throw new SQLException("Cliente no encontrado con el email: " + email);
        }
        if (!cliente.getPassword().equals(password)) {
            throw new SQLException("Contraseña incorrecta para el cliente con email: " + email);
        }
        return cliente;
    }
    

    public void actualizarCliente(Cliente cliente) throws SQLException {
        Cliente clienteExistente = clienteDao.findClienteByEmail(cliente.getEmail());
        
        if (clienteExistente != null) {
            clienteDao.updateCliente(cliente);
        } else {
            throw new SQLException("Cliente no encontrado con el email: " + cliente.getEmail());
        }
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

    public Cliente encontrarClienteByEmail(String email) throws SQLException {
        return clienteDao.findClienteByEmail(email);
    }

}

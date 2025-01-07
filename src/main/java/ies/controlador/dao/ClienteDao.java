package ies.controlador.dao;

import java.sql.SQLException;
import java.util.List;

import ies.modelo.Cliente;

public interface ClienteDao {

    public void insertCliente(Cliente cliente) throws SQLException;
    public void updateCliente(Cliente cliente) throws SQLException;
    public void deleteCliente(Cliente cliente) throws SQLException;
    public Cliente findClienteByEmail(String email) throws SQLException;
    public Cliente findClienteById(int id) throws SQLException;
    public List<Cliente> findAllClientes() throws SQLException;

}

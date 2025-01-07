package ies.controlador.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ies.controlador.dao.ClienteDao;
import ies.modelo.Cliente;
import ies.utils.DatabaseConf;

public class JdbcClienteDao implements ClienteDao {

    final String INSERT_CLIENTE = "INSERT INTO clientes (dni, nombre, direccion, telefono, email, password) VALUES(?, ?, ?, ?, ?, ?)";
    final String UPDATE_CLIENTE = "UPDATE clientes SET dni = ?, nombre = ?, direccion = ?, telefono = ?, email = ?, password = ? WHERE id = ?";
    final String DELETE_CLIENTE = "DELETE FROM clientes WHERE id = ?";
    final String FIND_BY_ID = "SELECT id, dni, nombre, direccion, telefono, email, password FROM clientes WHERE id = ?";
    final String FIND_EMAIL = "SELECT clientes.id, clientes.dni, clientes.nombre, clientes.direccion, clientes.telefono, clientes.email, clientes.password FROM clientes WHERE clientes.email = ?";
    final String FIND_ALL = "SELECT clientes.id, clientes.dni, clientes.nombre, clientes.direccion, clientes.telefono, clientes.email, clientes.password FROM clientes";

    @Override
    public void insertCliente(Cliente cliente) throws SQLException {
        try (Connection conexion = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER,
                DatabaseConf.PASSWORD);
                PreparedStatement pstmtCliente = conexion.prepareStatement(INSERT_CLIENTE,
                        Statement.RETURN_GENERATED_KEYS)) {
            {
                // Insertar el Cliente
                pstmtCliente.setString(1, cliente.getDni());
                pstmtCliente.setString(2, cliente.getNombre());
                pstmtCliente.setString(3, cliente.getDireccion());
                pstmtCliente.setString(4, cliente.getTelefono());
                pstmtCliente.setString(5, cliente.getEmail());
                pstmtCliente.setString(6, cliente.getPassword());
                pstmtCliente.executeUpdate();
                System.out.println("Cliente insertado correctamente.");
                // Obtiene el ID generado por el AUTO_INCREMENT
                try (ResultSet generatedKeys = pstmtCliente.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cliente.setId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }

    @Override
    public void updateCliente(Cliente cliente) throws SQLException {
        try (Connection conexion = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement pstmtCliente = conexion.prepareStatement(UPDATE_CLIENTE)) {
            pstmtCliente.setString(1, cliente.getDni());
            pstmtCliente.setString(2, cliente.getNombre());
            pstmtCliente.setString(3, cliente.getDireccion());
            pstmtCliente.setString(4, cliente.getTelefono());
            pstmtCliente.setString(5, cliente.getEmail());
            pstmtCliente.setString(6, cliente.getPassword());
            pstmtCliente.setInt(7, cliente.getId());
    
            int rowsUpdated = pstmtCliente.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cliente actualizado correctamente.");
            } else {
                System.out.println("No se encontró un cliente con ese ID.");
            }
        }
    }

    @Override
    public void deleteCliente(Cliente cliente) throws SQLException {
        try (Connection conexion = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement pstmtCliente = conexion.prepareStatement(DELETE_CLIENTE)) {
            pstmtCliente.setInt(1, cliente.getId());
    
            int rowsDeleted = pstmtCliente.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Cliente eliminado correctamente.");
            } else {
                System.out.println("No se encontró un cliente con ese ID.");
            }
        }
    }

    @Override
    public Cliente findClienteById(int id) throws SQLException {
        try (Connection conexion = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER, DatabaseConf.PASSWORD);
             PreparedStatement pstmtCliente = conexion.prepareStatement(FIND_BY_ID)) {
            pstmtCliente.setInt(1, id);
            ResultSet rSet = pstmtCliente.executeQuery();
    
            if (!rSet.next()) {
                return null;
            }
    
            Cliente cliente = new Cliente(rSet.getInt("id"), rSet.getString("dni"), rSet.getString("nombre"),
                    rSet.getString("direccion"), rSet.getString("telefono"),
                    rSet.getString("email"), rSet.getString("password"));
            System.out.println("Cliente encontrado correctamente.");
            return cliente;
        }
    }
    

    @Override
    public Cliente findClienteByEmail(String email) throws SQLException {
        try (Connection conexion = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER,
                DatabaseConf.PASSWORD);
                PreparedStatement pstmtCliente = conexion.prepareStatement(FIND_EMAIL)) {
            pstmtCliente.setString(1, email);
            ResultSet rSet = pstmtCliente.executeQuery();

            if (!rSet.next()) {
                return null;
            }

            Cliente cliente = new Cliente(rSet.getInt("id"), rSet.getString("clientes.dni"),
                    rSet.getString("clientes.nombre"),
                    rSet.getString("clientes.direccion"), rSet.getString("clientes.telefono"),
                    rSet.getString("clientes.email"), rSet.getString("clientes.password"));

            System.out.print("\nCliente encontrado correctamente:");
            return cliente;
        }

    }

    @Override
    public List<Cliente> findAllClientes() throws SQLException {
        try (Connection conexion = DriverManager.getConnection(DatabaseConf.URL, DatabaseConf.USER,
                DatabaseConf.PASSWORD);
                PreparedStatement pstmtCliente = conexion.prepareStatement(FIND_ALL)) {
            ResultSet rSet = pstmtCliente.executeQuery();
            Cliente cliente = null;
            List<Cliente> clientes = new ArrayList<>();
            while (rSet.next()) {
                cliente = new Cliente(rSet.getInt("id"), rSet.getString("dni"), rSet.getString("clientes.nombre"),
                        rSet.getString("clientes.direccion"),
                        rSet.getString("clientes.telefono"), rSet.getString("clientes.email"),
                        rSet.getString("clientes.password"));
                clientes.add(cliente);
            }
            System.out.print("\nCliente encontrado correctamente:\n");
            return clientes;

        }
    }

}

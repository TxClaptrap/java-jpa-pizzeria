package ies.controlador.dao;

import java.sql.SQLException;
import java.util.List;

import ies.modelo.Ingrediente;
import ies.modelo.Producto;

public interface ProductoDao {

    public void insertProducto(Producto producto) throws SQLException;
    public void updateProducto(Producto producto) throws SQLException;
    public void deleteProducto(Producto producto) throws SQLException;
    public Producto findProductoById(int idProducto) throws SQLException;
    public List <Producto> findAllProductos() throws SQLException;
    public List <Ingrediente> findIngredientesByProducto(int idProducto) throws SQLException;
    public List <String> findAlergenosByIngrediente(int idIngrediente) throws SQLException;
    


}

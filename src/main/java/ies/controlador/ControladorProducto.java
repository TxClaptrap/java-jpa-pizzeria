package ies.controlador;

import java.sql.SQLException;
import java.util.List;

import ies.controlador.dao.ProductoDao;
import ies.controlador.dao.impl.JpaProductoDao;
import ies.modelo.Ingrediente;
import ies.modelo.Producto;

public class ControladorProducto {

ProductoDao productoDao = new JpaProductoDao();

    public void registrarProducto(Producto producto) throws SQLException {
        productoDao.insertProducto(producto);
    }

    public void actualizarProducto(Producto producto) throws SQLException {
        productoDao.updateProducto(producto);
    }

    public void borrarProducto(Producto producto) throws SQLException {
        productoDao.deleteProducto(producto);
    }

    public Producto encontrarProductoById(int idProducto) throws SQLException {
        return productoDao.findProductoById(idProducto);
    }

    public List<Producto> encontrarAllProductos() throws SQLException {
        return productoDao.findAllProductos();
    }

    public List<Ingrediente> encontrarIngredientesByProducto(int idProducto) throws SQLException {
        return productoDao.findIngredientesByProducto(idProducto);
    }

    public List<String> encontrarAlergenosByIngrediente(int idIngrediente) throws SQLException {
        return productoDao.findAlergenosByIngrediente(idIngrediente);
    }

}

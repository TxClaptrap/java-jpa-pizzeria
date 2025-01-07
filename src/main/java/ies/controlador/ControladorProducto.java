package ies.controlador;

import java.sql.SQLException;
import java.util.List;

import ies.controlador.dao.ProductoDao;
import ies.controlador.dao.impl.JdbcProductoDao;
import ies.modelo.Producto;

public class ControladorProducto {

ProductoDao productoDao = new JdbcProductoDao();

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

    /* 
    GestorFicheros gestorFicheros;

    public ControladorProducto() {
        gestorFicheros = new GestorFicheros();
    }

    //Métodos de encapsulado, para no hacerlo todo desde GestorFicheros
    public boolean exportarIngredientesCSV(List<Ingrediente> ingredientes)
            throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, FileNotFoundException {
        return gestorFicheros.exportarCSV(ingredientes);
    }

    public List<Ingrediente> importarIngredientesCSV() throws FileNotFoundException, IOException {
        return gestorFicheros.importarCSV();
    }
        */

}

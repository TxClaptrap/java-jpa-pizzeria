package ies.controlador;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ies.modelo.Producto;

@XmlRootElement(name = "productos")
public class ProductosWrapper {
    private List<Producto> productos;

    @XmlElement(name = "producto")
    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
package ies.modelo;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class LineaPedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private int cantidad;
    @OneToOne
    private Producto producto;
    private Double precio;

    public LineaPedido(int cantidad, Producto producto) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.precio = producto.getPrecio() * cantidad;
    }

    public LineaPedido(int cantidad, Producto producto, Double precio) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.precio = precio;
    }

    public LineaPedido(List<LineaPedido> listaLineaPedidos) {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Double getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return "LineaPedido [id=" + id + ", cantidad=" + cantidad + ", producto=" + producto + ", precio=" + precio
                + "]";
    }

    

}

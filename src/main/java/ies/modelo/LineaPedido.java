package ies.modelo;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class LineaPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private int cantidad;
    @OneToOne (fetch = FetchType.LAZY)
    private Producto producto;
    private Double precio;
    @ManyToOne
    private Pedido pedido;

    public LineaPedido(int cantidad, Producto producto) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.precio = producto.getPrecio() * cantidad;
    }

    public LineaPedido(List<LineaPedido> listaLineaPedidos) {

    }

    public LineaPedido() {
        
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public String toString() {
        return "LineaPedido [id=" + id + ", cantidad=" + cantidad + ", producto=" + producto + ", precio=" + precio
                + "]";
    }

}

package ies.modelo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY) 
    private List<LineaPedido> listaLineaPedidos; 
    private double precioTotal;
    @OneToOne(cascade = CascadeType.ALL)
    private Pagable metodoPago;
    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    @ManyToOne   
    private Cliente cliente;

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        fecha = LocalDate.now();
        listaLineaPedidos = new ArrayList<>();
        precioTotal = 0;
        estado = EstadoPedido.PENDIENTE;
    }

    public Pedido(int id, Cliente cliente) {
        this.cliente = cliente;
        fecha = LocalDate.now();
        listaLineaPedidos = new ArrayList<>();
        precioTotal = 0;
        estado = EstadoPedido.PENDIENTE;
    }

    public Pedido() {

    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }


    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public List<LineaPedido> getListaLineaPedidos() {
        return listaLineaPedidos;
    }

    public void setListaLineaPedidos(List<LineaPedido> listaLineaPedidos) {
        this.listaLineaPedidos = listaLineaPedidos;
    }

    public Pagable getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(Pagable metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Pedido [id=" + id + ", fecha=" + fecha + ", listaLineaPedidos=" + listaLineaPedidos + ", precioTotal="
                + precioTotal + ", metodoPago=" + metodoPago + ", estado=" + estado + ", cliente=" + cliente + "]";
    }

}



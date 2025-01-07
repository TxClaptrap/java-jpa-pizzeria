package ies.modelo;

import java.util.List;

public class ProductosSeparados {
    private List<Producto> pizzas;
    private List<Producto> pastas;
    private List<Producto> bebidas;

    public ProductosSeparados(List<Producto> pizzas, List<Producto> pastas, List<Producto> bebidas) {
        this.pizzas = pizzas;
        this.pastas = pastas;
        this.bebidas = bebidas;
    }

    public List<Producto> getPizzas() {
        return pizzas;
    }

    public List<Producto> getPastas() {
        return pastas;
    }

    public List<Producto> getBebidas() {
        return bebidas;
    }
}

package ies.modelo;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
public class Pasta  extends Producto {

    @ManyToMany //Anotación para que se enganche con la tabla intermedia. @JoinTable(name = Producto-Ingrediente" JoinColumn = @JoinColumn(name = "producto_id"))
    private List<Ingrediente> listaIngredientes;

    public Pasta(String nombre, double precio, List<Ingrediente> listaIngredientes) {
        super(nombre, precio);
        this.listaIngredientes = listaIngredientes;
    }

    public Pasta(int id, String nombre, double precio, List<Ingrediente> listaIngredientes) {
        super(id, nombre, precio);
        this.listaIngredientes = listaIngredientes;
    }
    
    public Pasta() {
        super(null, 0);
    }

    public List<Ingrediente> getListaIngredientes() {
        return listaIngredientes;
    }

    public void setListaIngredientes(List<Ingrediente> listaIngredientes) {
        this.listaIngredientes = listaIngredientes;
    }

    @Override
    public String toString() {
        return this.getId() + " - " + this.getNombre() + ": " + this.getPrecio() + "$" + " [listaIngredientes=" + listaIngredientes + "]";
    }

    
    
}

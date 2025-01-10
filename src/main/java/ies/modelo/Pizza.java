package ies.modelo;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;

@Entity
public class Pizza  extends Producto {

    @Enumerated(EnumType.STRING)
    private SIZE tamano;

    @ManyToMany //Anotación para que se enganche con la tabla intermedia. @JoinTable(name = Producto-Ingrediente" JoinColumn = @JoinColumn(name = "producto_id"))
    private List<Ingrediente> listaIngredientes;
    
    public Pizza(String nombre, double precio, SIZE tamano, List<Ingrediente> listaIngredientes) {
        super(nombre, precio);
        this.tamano = tamano;
        this.listaIngredientes = listaIngredientes;
    }

    public Pizza(int id, String nombre, double precio, SIZE tamano, List<Ingrediente> listaIngredientes) {
        super(id, nombre, precio);
        this.tamano = tamano;
        this.listaIngredientes = listaIngredientes;
    }
    
    public Pizza() {
        super(null, 0);
    }


    public SIZE getTamano() {
        return tamano;
    }
    public void setTamano(SIZE tamano) {
        this.tamano = tamano;
    }


    public List<Ingrediente> getListaIngredientes() {
        return listaIngredientes;
    }
    public void setListaIngredientes(List<Ingrediente> listaIngredientes) {
        this.listaIngredientes = listaIngredientes;
    }

    @Override
    public String toString() {
        return this.getId() + " - " + this.getNombre() + ": " + this.getPrecio() + "$" + " [tamano=" + tamano + ", listaIngredientes=" + listaIngredientes + "]";
    }



    

}

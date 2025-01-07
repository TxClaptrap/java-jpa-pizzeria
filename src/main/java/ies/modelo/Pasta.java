package ies.modelo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class Pasta  extends Producto {
    @XmlElementWrapper(name = "ingredientes")
    @XmlElement(name = "ingrediente")
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

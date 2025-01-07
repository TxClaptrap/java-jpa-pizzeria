package ies.modelo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class Pizza  extends Producto {
    private SIZE tamano;
    @XmlElementWrapper(name = "ingredientes")
    @XmlElement(name = "ingrediente")
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

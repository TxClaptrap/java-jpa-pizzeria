package ies.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class Bebida extends Producto{
    @Enumerated(EnumType.STRING)
    private SIZE tamano;

    public Bebida(String nombre, double precio, SIZE tamano) {
        super(nombre, precio);
        this.tamano = tamano;
    }

    public Bebida(int id, String nombre, double precio, SIZE tamano) {
        super(id, nombre, precio);
        this.tamano = tamano;
    }
    
    public Bebida () {
    }

    public SIZE getTamano() {
        return tamano;
    }

    public void setTamano(SIZE tamano) {
        this.tamano = tamano;
    }

    @Override
    public String toString() {
        return this.getId() + " - " + this.getNombre() + ": " + this.getPrecio() + "$" + " Bebida [tamano=" + tamano + "]";
    }

    
    
}

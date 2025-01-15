package ies.modelo;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(unique = true, nullable = false)
    private String nombre;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "alergenos_ingredientes")
    private List<String> alergenos;

    public Ingrediente() {
    }

    public Ingrediente(String nombre, List<String> alergenos) {
        this.nombre = nombre;
        this.alergenos = alergenos;
    }

    public Ingrediente(int id, String nombre, List<String> alergenos) {
        this.id = id;
        this.nombre = nombre;
        this.alergenos = alergenos;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public List<String> getAlergenos() {
        return alergenos;
    }
    public void setAlergenos(List<String> alergenos) {
        this.alergenos = alergenos;
    }

    @Override
    public String toString() {
        return "Ingrediente [id=" + id + ", nombre=" + nombre + ", alergenos=" + alergenos + "]";
    }

}

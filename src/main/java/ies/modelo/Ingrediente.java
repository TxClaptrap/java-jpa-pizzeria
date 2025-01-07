package ies.modelo;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;

import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Ingrediente {
    @CsvBindByName(column= "ID")
    private int id;
    @CsvBindByName(column= "NOMBRE")
    private String nombre;
    @CsvBindAndSplitByName(column = "ALERGENOS", writeDelimiter = ",", elementType = String.class)
    @XmlElementWrapper(name = "alergenos")
    @XmlElement(name = "alergeno")
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

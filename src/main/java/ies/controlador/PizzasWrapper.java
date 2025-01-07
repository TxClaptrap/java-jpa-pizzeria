package ies.controlador;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ies.modelo.Pizza;

@XmlRootElement(name = "pizzas")
@XmlAccessorType(XmlAccessType.FIELD)
public class PizzasWrapper {

    @XmlElement(name = "pizza")
    private List<Pizza> pizzas = new ArrayList<>();

    public PizzasWrapper(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

    public PizzasWrapper() {
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public void setPizzas(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

    
}

package ies.modelo;

import jakarta.persistence.Entity;

@Entity
public class Pagar_Efectivo extends Pagable {

    @Override
    public void pagar(double cantidad) {
        System.out.println("Pago en efectivo: " + cantidad);
    }
    
}

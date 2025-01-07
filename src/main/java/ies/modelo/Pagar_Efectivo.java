package ies.modelo;

public class Pagar_Efectivo implements Pagable {

    @Override
    public void pagar(double cantidad) {
        System.out.println("Pago en efectivo: " + cantidad);
    }
    
}

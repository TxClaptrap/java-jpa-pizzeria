package ies.modelo;

public class Pagar_Tarjeta implements Pagable {

    @Override
    public void pagar(double cantidad) {
        System.out.println("Pago en tarjeta: " + cantidad);
    }
    
}

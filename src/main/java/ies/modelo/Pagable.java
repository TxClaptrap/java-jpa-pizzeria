package ies.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class Pagable {

    //TODO pon el id  
    dd

    abstract public void pagar(double cantidad);
    
} 

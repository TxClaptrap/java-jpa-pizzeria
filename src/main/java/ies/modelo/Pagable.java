package ies.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Pagable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    abstract public void pagar(double cantidad);
    
} 

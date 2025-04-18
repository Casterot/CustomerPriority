package com.customerpriority.sig.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cargo")
public class Cargo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    private int idcargo;

    @Column(name = "nombre_cargo", length = 50, nullable = false)
    private String nombreCargo;

    @Column(name = "personal_a_cargo", length = 50, nullable = false)
    private String personalCargo;


    public String getPersonalCargo() {
        return personalCargo;
    }

    public void setPersonalCargo(String personalCargo) {
        this.personalCargo = personalCargo;
    }

    public int getIdcargo() {
        return idcargo;
    }

    public void setIdcargo(int idcargo) {
        this.idcargo = idcargo;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

    
}

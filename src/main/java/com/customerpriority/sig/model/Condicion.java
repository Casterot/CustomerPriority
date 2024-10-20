package com.customerpriority.sig.model;

import jakarta.persistence.*;

@Entity
@Table(name = "condicion")
public class Condicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCondicion;

    @Column(name = "nombre_condicion", length = 50, nullable = false)
    private String nombreCondicion;


    public int getIdCondicion() {
        return idCondicion;
    }

    public void setIdCondicion(int idCondicion) {
        this.idCondicion = idCondicion;
    }

    public String getNombreCondicion() {
        return nombreCondicion;
    }

    public void setNombreCondicion(String nombreCondicion) {
        this.nombreCondicion = nombreCondicion;
    }

}
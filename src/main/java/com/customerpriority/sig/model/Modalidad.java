package com.customerpriority.sig.model;

import jakarta.persistence.*;

@Entity
@Table(name = "modalidad")
public class Modalidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idModalidad;

    @Column(name = "nombre_modalidad", length = 50, nullable = false)
    private String nombreModalidad;


    public int getIdModalidad() {
        return idModalidad;
    }

    public void setIdModalidad(int idModalidad) {
        this.idModalidad = idModalidad;
    }

    public String getNombreModalidad() {
        return nombreModalidad;
    }

    public void setNombreModalidad(String nombreModalidad) {
        this.nombreModalidad = nombreModalidad;
    }

    
}
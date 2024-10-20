package com.customerpriority.sig.model;

import jakarta.persistence.*;

@Entity
@Table(name = "centro")
public class Centro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCentro;

    @Column(name = "nombre_centro", length = 255, nullable = false)
    private String nombreCentro;



    public int getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(int idCentro) {
        this.idCentro = idCentro;
    }

    public String getNombreCentro() {
        return nombreCentro;
    }

    public void setNombreCentro(String nombreCentro) {
        this.nombreCentro = nombreCentro;
    }

}
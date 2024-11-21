package com.customerpriority.sig.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_gestion")
public class TipoGestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idGestion;

    @Column(name = "nombre_gestion", length = 50, nullable = false)
    private String nombreGestion;

    public int getIdGestion() {
        return idGestion;
    }

    public void setIdGestion(int idGestion) {
        this.idGestion = idGestion;
    }

    public String getNombreGestion() {
        return nombreGestion;
    }

    public void setNombreGestion(String nombreGestion) {
        this.nombreGestion = nombreGestion;
    }

    
}
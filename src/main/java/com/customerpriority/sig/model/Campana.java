package com.customerpriority.sig.model;

import jakarta.persistence.*;

@Entity
@Table(name = "campanas")
public class Campana {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCampana;

    @Column(name = "segmento", length = 50)
    private String segmento;

    @Column(name = "nombre_campana", length = 50, nullable = false)
    private String nombreCampana;

    @Column(name = "tipo_gestion", length = 50)
    private String gestion;

    @Column(name = "estado", nullable = false)
    private int estado;

    public int getIdCampana() {
        return idCampana;
    }

    public void setIdCampana(int idCampana) {
        this.idCampana = idCampana;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getNombreCampana() {
        return nombreCampana;
    }

    public void setNombreCampana(String nombreCampana) {
        this.nombreCampana = nombreCampana;
    }

    public String getGestion() {
        return gestion;
    }

    public void setGestion(String gestion) {
        this.gestion = gestion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    
}
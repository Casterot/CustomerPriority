package com.customerpriority.sig.model;

import jakarta.persistence.*;


@Entity
@Table(name = "horario")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHorario;

    @Column(name = "nombre_horario", length = 20, nullable = false)
    private String nombreHorario;

    @ManyToOne
    @JoinColumn(name = "id_turno")
    private Turno turno;

    @ManyToOne
    @JoinColumn(name = "id_condicion")
    private Condicion condicion;


    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public String getNombreHorario() {
        return nombreHorario;
    }

    public void setNombreHorario(String nombreHorario) {
        this.nombreHorario = nombreHorario;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Condicion getCondicion() {
        return condicion;
    }

    public void setCondicion(Condicion condicion) {
        this.condicion = condicion;
    }

    
}
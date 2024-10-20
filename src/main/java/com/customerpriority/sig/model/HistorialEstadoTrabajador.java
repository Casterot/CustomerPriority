package com.customerpriority.sig.model;

import java.sql.Date;

import jakarta.persistence.*;



@Entity
@Table(name = "historial_estado_trabajador")
public class HistorialEstadoTrabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHistorial;

    @ManyToOne
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;

    @Column(name = "fecha_estado", nullable = false)
    private Date fechaEstado;

    @Column(name = "estado", length = 100, nullable = false)
    private String estado;

    @Column(name = "observacion", length = 255)
    private String observacion;
    
}
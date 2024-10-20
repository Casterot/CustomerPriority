package com.customerpriority.sig.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_bitel")
public class UsuarioBitel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBitel;

    @Column(name = "ubitel", length = 50, nullable = false)
    private String ubitel;

    @ManyToOne
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;

    @Column(name = "estado", columnDefinition = "ENUM('Activo', 'Cancelado')", nullable = false)
    private String estado;


    public int getIdBitel() {
        return idBitel;
    }

    public void setIdBitel(int idBitel) {
        this.idBitel = idBitel;
    }

    public String getUbitel() {
        return ubitel;
    }

    public void setUbitel(String ubitel) {
        this.ubitel = ubitel;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
}
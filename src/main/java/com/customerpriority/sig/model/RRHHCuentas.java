package com.customerpriority.sig.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rrhh_cuentas")
public class RRHHCuentas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCuenta;

    @ManyToOne
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;

    @Column(name = "numero_cuenta", length = 255, nullable = false)
    private String numeroCuenta;

    @Column(name = "sueldo", nullable = false)
    private double sueldo;

    @Column(name = "ruc", length = 11, nullable = false)
    private String ruc;

    @Column(name = "usuario_sol", length = 255, nullable = false)
    private String usuarioSol;

    @Column(name = "clave_sol", length = 255, nullable = false)
    private String claveSol;

    @Column(name = "emite_propio_recibo", length = 2)
    private String emitePropioRecibo;

    @ManyToOne
    @JoinColumn(name = "id_banco")
    private RRHHBanco banco;


    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getUsuarioSol() {
        return usuarioSol;
    }

    public void setUsuarioSol(String usuarioSol) {
        this.usuarioSol = usuarioSol;
    }

    public String getClaveSol() {
        return claveSol;
    }

    public void setClaveSol(String claveSol) {
        this.claveSol = claveSol;
    }

    public String getEmitePropioRecibo() {
        return emitePropioRecibo;
    }

    public void setEmitePropioRecibo(String emitePropioRecibo) {
        this.emitePropioRecibo = emitePropioRecibo;
    }

    public RRHHBanco getBanco() {
        return banco;
    }

    public void setBanco(RRHHBanco banco) {
        this.banco = banco;
    }

    
}
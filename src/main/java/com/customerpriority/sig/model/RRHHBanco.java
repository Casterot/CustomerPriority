package com.customerpriority.sig.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rrhh_banco")
public class RRHHBanco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBanco;

    @Column(name = "codigo", length = 3, nullable = false)
    private String codigo;

    @Column(name = "banco", length = 255, nullable = false)
    private String banco;


    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    
}
package com.customerpriority.sig.model;

import jakarta.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "trabajadores")
public class Trabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trabajador")
    private int idTrabajador;

    @Column(name = "documento", length = 15, nullable = false)
    private String documento;

    @Column(name = "apellido_paterno", length = 100, nullable = false)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", length = 100, nullable = false)
    private String apellidoMaterno;

    @Column(name = "nombre_completo", length = 100, nullable = false)
    private String nombreCompleto;

    @Column(name = "fecha_nacimiento", nullable = false)
    private Date fechaNacimiento;

    @Column(name = "telefono", length = 9)
    private String telefono;

    @Column(name = "correo", length = 255)
    private String correo;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "id_distrito")
    private Distrito distrito;

    @ManyToOne
    @JoinColumn(name = "id_genero")
    private Genero genero;    
    
    @ManyToOne
    @JoinColumn(name = "id_tipo_documento")
    private TipoDocumento tipoDocumento;

    @ManyToOne
    @JoinColumn(name = "id_segmento")
    private Segmento segmento;

    // Relación autorreferencial: cada trabajador puede tener un jefe directo
    @ManyToOne
    @JoinColumn(name = "id_jefe_directo", referencedColumnName = "id_trabajador")
    private Trabajador jefeDirecto;

    // Relación inversa: un trabajador puede ser jefe de varios trabajadores
    @OneToMany(mappedBy = "jefeDirecto")
    private List<Trabajador> subordinados;

    @ManyToOne
    @JoinColumn(name = "id_horario")
    private Horario horario;

    @ManyToOne
    @JoinColumn(name = "id_centro")
    private Centro centro;

    @ManyToOne
    @JoinColumn(name = "id_modalidad")
    private Modalidad modalidad;

    @ManyToOne
    @JoinColumn(name = "id_jornada")
    private Jornada jornada;

    @ManyToOne
    @JoinColumn(name = "id_cargo")
    private Cargo cargo;

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Segmento getSegmento() {
        return segmento;
    }

    public void setSegmento(Segmento segmento) {
        this.segmento = segmento;
    }

    public Trabajador getJefeDirecto() {
        return jefeDirecto;
    }

    public void setJefeDirecto(Trabajador jefeDirecto) {
        this.jefeDirecto = jefeDirecto;
    }

    public List<Trabajador> getSubordinados() {
        return subordinados;
    }

    public void setSubordinados(List<Trabajador> subordinados) {
        this.subordinados = subordinados;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Centro getCentro() {
        return centro;
    }

    public void setCentro(Centro centro) {
        this.centro = centro;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

}
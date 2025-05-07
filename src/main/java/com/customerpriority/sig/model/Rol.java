package com.customerpriority.sig.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRol;

    @Column(name = "nombre_rol", length = 50, nullable = false)
    private String nombreRol;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "roles_permisos",
        joinColumns = @JoinColumn(name = "rol_id"),
        inverseJoinColumns = @JoinColumn(name = "permiso_id")
    )
    @OrderBy("nombre ASC")
    private Set<Permiso> permisos = new HashSet<>();

    // Getters y setters originales
    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    // Nuevos getters y setters para permisos
    public Set<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(Set<Permiso> permisos) {
        this.permisos = permisos;
    }

    // Métodos de utilidad
    public void agregarPermiso(Permiso permiso) {
        this.permisos.add(permiso);
    }

    public void removerPermiso(Permiso permiso) {
        this.permisos.remove(permiso);
    }
}

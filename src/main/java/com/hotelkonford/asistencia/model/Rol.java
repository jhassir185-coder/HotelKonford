package com.hotelkonford.asistencia.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ROL")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "nombre", length = 255)
    private String nombre;

    public Rol() {}

    public Rol(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdRol() { return idRol; }
    public void setIdRol(Integer idRol) { this.idRol = idRol; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

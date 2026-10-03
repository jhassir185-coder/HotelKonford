package com.hotelkonford.asistencia.model;

import jakarta.persistence.*;

@Entity
@Table(name = "AREA")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_area")
    private Integer idArea;

    @Column(name = "nombre", length = 255)
    private String nombre;

    public Area() {}

    public Area(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdArea() { return idArea; }
    public void setIdArea(Integer idArea) { this.idArea = idArea; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

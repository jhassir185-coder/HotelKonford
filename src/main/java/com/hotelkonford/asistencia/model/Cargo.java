package com.hotelkonford.asistencia.model;

import jakarta.persistence.*;

@Entity
@Table(name = "CARGO")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    private Integer idCargo;

    @Column(name = "nombre", length = 255)
    private String nombre;

    public Cargo() {}

    public Cargo(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdCargo() { return idCargo; }
    public void setIdCargo(Integer idCargo) { this.idCargo = idCargo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

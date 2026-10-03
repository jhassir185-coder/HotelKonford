package com.hotelkonford.asistencia.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TIPO_PERMISO")
public class TipoPermiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_permiso")
    private Integer idTipoPermiso;

    @Column(name = "nombre", length = 255)
    private String nombre;

    public TipoPermiso() {}

    public TipoPermiso(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdTipoPermiso() { return idTipoPermiso; }
    public void setIdTipoPermiso(Integer idTipoPermiso) { this.idTipoPermiso = idTipoPermiso; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre)  { this.nombre = nombre; }
     }
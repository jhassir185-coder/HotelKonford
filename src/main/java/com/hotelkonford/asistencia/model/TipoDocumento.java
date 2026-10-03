package com.hotelkonford.asistencia.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TIPO_DOCUMENTO")
public class TipoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_documento")
    private Integer idTipoDocumento;

    @Column(name = "nombre", length = 255)
    private String nombre;

    public TipoDocumento() {}

    public TipoDocumento(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdTipoDocumento() { return idTipoDocumento; }
    public void setIdTipoDocumento(Integer idTipoDocumento) { this.idTipoDocumento = idTipoDocumento; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

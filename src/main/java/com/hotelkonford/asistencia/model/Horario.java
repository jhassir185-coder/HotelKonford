package com.hotelkonford.asistencia.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "HORARIO")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Integer id;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "hora_entrada")
    private LocalTime horaEntrada;

    @Column(name = "hora_salida")
    private LocalTime horaSalida;

    @Column(name = "tolerancia")
    private Integer tolerancia = 15;

    public Horario() {}

    public Horario(Integer id, String nombre, LocalTime horaEntrada, LocalTime horaSalida, Integer tolerancia) {
        this.id = id;
        this.nombre = nombre;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.tolerancia = tolerancia;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalTime getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(LocalTime horaEntrada) { this.horaEntrada = horaEntrada; }

    public LocalTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalTime horaSalida) { this.horaSalida = horaSalida; }

    public Integer getTolerancia() { return tolerancia; }
    public void setTolerancia(Integer tolerancia) { this.tolerancia = tolerancia; }
}
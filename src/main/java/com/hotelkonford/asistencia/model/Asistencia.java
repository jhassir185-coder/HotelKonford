package com.hotelkonford.asistencia.model;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "ASISTENCIA")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asistencia")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "hora_ingreso")
    private LocalTime horaIngreso;

    @Column(name = "hora_salida")
    private LocalTime horaSalida;

    @Column(name = "estado", length = 50)
    private String estado;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    public Asistencia() {}

    // Método utilitario para cálculo dinámico de minutos de tardanza
    public long getMinutosTardanza() {
        if (this.horaIngreso != null && this.empleado != null && this.empleado.getHorario() != null) {
            LocalTime horaEsperada = this.empleado.getHorario().getHoraEntrada();
            if (this.horaIngreso.isAfter(horaEsperada)) {
                return Duration.between(horaEsperada, this.horaIngreso).toMinutes();
            }
        }
        return 0;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraIngreso() { return horaIngreso; }
    public void setHoraIngreso(LocalTime horaIngreso) { this.horaIngreso = horaIngreso; }

    public LocalTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalTime horaSalida) { this.horaSalida = horaSalida; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
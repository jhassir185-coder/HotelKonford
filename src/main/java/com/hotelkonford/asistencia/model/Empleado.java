package com.hotelkonford.asistencia.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "EMPLEADO")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cargo")
    private Cargo cargoObj;

    @ManyToOne
    @JoinColumn(name = "id_rol")
    private Rol rolObj;

    @ManyToOne
    @JoinColumn(name = "id_area")
    private Area areaObj;

    @ManyToOne
    @JoinColumn(name = "id_horario")
    private Horario horario;

    @ManyToOne
    @JoinColumn(name = "id_tipo_documento")
    private TipoDocumento tipoDocumentoObj;

    @Column(name = "numero_documento", length = 20)
    private String dni;

    @Column(name = "nombres", length = 255)
    private String nombres;

    @Column(name = "apellidos", length = 255)
    private String apellidos;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "telefono", length = 50)
    private String telefono;

    @Column(name = "direccion", length = 200)
    private String direccion;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso = LocalDate.now();

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "activo")
    private Boolean activo = true;

    public Empleado() {}

    // Métodos de compatibilidad con Frontend / Controllers existentes
    public String getNombreCompleto() {
        return (nombres != null ? nombres : "") + " " + (apellidos != null ? apellidos : "");
    }

    public String getCargo() {
        return cargoObj != null ? cargoObj.getNombre() : "Sin Cargo";
    }

    public String getArea() {
        return areaObj != null ? areaObj.getNombre() : "Sin Área";
    }

    public String getRol() {
        return rolObj != null ? rolObj.getNombre() : "TRABAJADOR";
    }

    public String getTipoDocumento() {
        return tipoDocumentoObj != null ? tipoDocumentoObj.getNombre() : "DNI";
    }

    // Getters y Setters Estándar
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Cargo getCargoObj() { return cargoObj; }
    public void setCargoObj(Cargo cargoObj) { this.cargoObj = cargoObj; }

    public Rol getRolObj() { return rolObj; }
    public void setRolObj(Rol rolObj) { this.rolObj = rolObj; }

    public Area getAreaObj() { return areaObj; }
    public void setAreaObj(Area areaObj) { this.areaObj = areaObj; }

    public Horario getHorario() { return horario; }
    public void setHorario(Horario horario) { this.horario = horario; }

    public TipoDocumento getTipoDocumentoObj() { return tipoDocumentoObj; }
    public void setTipoDocumentoObj(TipoDocumento tipoDocumentoObj) { this.tipoDocumentoObj = tipoDocumentoObj; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
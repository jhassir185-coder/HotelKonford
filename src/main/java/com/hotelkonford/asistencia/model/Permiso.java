package com.hotelkonford.asistencia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PERMISO")
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "id_tipo_permiso")
    private TipoPermiso tipoPermisoObj;

    @Column(name = "motivo", columnDefinition = "TEXT")
    private String motivo;

    @Column(name = "estado", length = 20)
    private String estado = "PENDIENTE";

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    @Column(name = "comentario_jefe", columnDefinition = "TEXT")
    private String comentarioJefa;

    @Column(name = "comprobante_ruta", length = 500)
    private String comprobanteRuta;

    public Permiso() {}

    // Método adaptador para compatibilidad con la vista HTML
    public String getTipoPermiso() {
        return tipoPermisoObj != null ? tipoPermisoObj.getNombre() : "PERSONAL";
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public TipoPermiso getTipoPermisoObj() { return tipoPermisoObj; }
    public void setTipoPermisoObj(TipoPermiso tipoPermisoObj) { this.tipoPermisoObj = tipoPermisoObj; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public String getComentarioJefa() { return comentarioJefa; }
    public void setComentarioJefa(String comentarioJefa) { this.comentarioJefa = comentarioJefa; }

    public String getComprobanteRuta() { return comprobanteRuta; }
    public void setComprobanteRuta(String comprobanteRuta) { this.comprobanteRuta = comprobanteRuta; }
}
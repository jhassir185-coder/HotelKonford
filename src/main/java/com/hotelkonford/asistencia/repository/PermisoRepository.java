package com.hotelkonford.asistencia.repository;

import com.hotelkonford.asistencia.model.Empleado;
import com.hotelkonford.asistencia.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PermisoRepository extends JpaRepository<Permiso, Integer> {
    List<Permiso> findByEstado(String estado);
    List<Permiso> findByEmpleado(Empleado empleado);
}
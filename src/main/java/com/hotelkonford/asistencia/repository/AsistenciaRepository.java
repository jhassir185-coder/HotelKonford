package com.hotelkonford.asistencia.repository;

import com.hotelkonford.asistencia.model.Asistencia;
import com.hotelkonford.asistencia.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Integer> {
    List<Asistencia> findByEmpleado(Empleado empleado);
    Optional<Asistencia> findByEmpleadoAndFecha(Empleado empleado, LocalDate fecha);
    List<Asistencia> findByFecha(LocalDate fecha);
}
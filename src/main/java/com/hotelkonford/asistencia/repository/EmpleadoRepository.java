package com.hotelkonford.asistencia.repository;

import com.hotelkonford.asistencia.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    Optional<Empleado> findByDni(String dni);
    boolean existsByDni(String dni);
}
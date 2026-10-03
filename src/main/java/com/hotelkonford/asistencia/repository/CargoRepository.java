package com.hotelkonford.asistencia.repository;

import com.hotelkonford.asistencia.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CargoRepository extends JpaRepository<Cargo, Integer> {
    Optional<Cargo> findByNombre(String nombre);
}

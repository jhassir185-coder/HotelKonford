package com.hotelkonford.asistencia.repository;

import com.hotelkonford.asistencia.model.TipoPermiso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TipoPermisoRepository extends JpaRepository<TipoPermiso, Integer> {
    Optional<TipoPermiso> findByNombre(String nombre);
}

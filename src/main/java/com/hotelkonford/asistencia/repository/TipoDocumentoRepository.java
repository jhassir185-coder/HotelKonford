package com.hotelkonford.asistencia.repository;

import com.hotelkonford.asistencia.model.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer> {
    Optional<TipoDocumento> findByNombre(String nombre);
}

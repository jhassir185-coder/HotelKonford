package com.hotelkonford.asistencia.repository;

import com.hotelkonford.asistencia.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HorarioRepository extends JpaRepository<Horario, Integer> {
}
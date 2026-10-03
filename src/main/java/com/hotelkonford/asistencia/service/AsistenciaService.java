package com.hotelkonford.asistencia.service;

import com.hotelkonford.asistencia.model.Asistencia;
import com.hotelkonford.asistencia.model.Empleado;
import com.hotelkonford.asistencia.repository.AsistenciaRepository;
import com.hotelkonford.asistencia.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final EmpleadoRepository empleadoRepository;

    public AsistenciaService(AsistenciaRepository asistenciaRepository, EmpleadoRepository empleadoRepository) {
        this.asistenciaRepository = asistenciaRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public String registrarMarca(String dni) {
        Optional<Empleado> empOpt = empleadoRepository.findByDni(dni);
        if (empOpt.isEmpty()) return "Empleado no encontrado";

        Empleado emp = empOpt.get();
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        Optional<Asistencia> asistenciaOpt = asistenciaRepository.findByEmpleadoAndFecha(emp, hoy);

        if (asistenciaOpt.isEmpty()) {
            // Es ENTRADA
            Asistencia nueva = new Asistencia();
            nueva.setEmpleado(emp);
            nueva.setFecha(hoy);
            nueva.setHoraIngreso(ahora);

            // Lógica de tardanza / fuera de turno
            if (emp.getHorario() != null) {
                LocalTime limite = emp.getHorario().getHoraEntrada().plusMinutes(emp.getHorario().getTolerancia());
                LocalTime horaSalida = emp.getHorario().getHoraSalida();

                if (ahora.isAfter(horaSalida)) {
                    nueva.setEstado("FUERA_DE_TURNO");
                } else if (ahora.isAfter(limite)) {
                    nueva.setEstado("TARDANZA");
                } else {
                    nueva.setEstado("PUNTUAL");
                }
            } else {
                nueva.setEstado("PUNTUAL");
            }

            asistenciaRepository.save(nueva);
            return "Entrada registrada: " + ahora + " (" + nueva.getEstado() + ")";
        } else {
            // Es SALIDA
            Asistencia asis = asistenciaOpt.get();
            if (asis.getHoraSalida() != null) return "Ya registraste tu salida hoy";

            asis.setHoraSalida(ahora);
            asistenciaRepository.save(asis);
            return "Salida registrada: " + ahora;
        }
    }
}
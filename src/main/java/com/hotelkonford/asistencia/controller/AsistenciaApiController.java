package com.hotelkonford.asistencia.controller;

import com.hotelkonford.asistencia.service.AsistenciaService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/asistencia")
public class AsistenciaApiController {

    private final AsistenciaService asistenciaService;

    public AsistenciaApiController(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;
    }

    @PostMapping("/marcar")
    public Map<String, String> marcar(@RequestBody Map<String, String> payload) {
        String dni = payload.get("dni");
        String mensaje = asistenciaService.registrarMarca(dni);
        
        boolean success = !mensaje.startsWith("ERROR");
        return Map.of(
            "status", success ? "success" : "error",
            "message", mensaje
        );
    }
}

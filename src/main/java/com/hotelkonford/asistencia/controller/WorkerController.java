package com.hotelkonford.asistencia.controller;

import com.hotelkonford.asistencia.model.Empleado;
import com.hotelkonford.asistencia.model.Permiso;
import com.hotelkonford.asistencia.model.TipoPermiso;
import com.hotelkonford.asistencia.repository.AsistenciaRepository;
import com.hotelkonford.asistencia.repository.EmpleadoRepository;
import com.hotelkonford.asistencia.repository.PermisoRepository;
import com.hotelkonford.asistencia.repository.TipoPermisoRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/trabajador")
public class WorkerController {

    private final PermisoRepository permisoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final TipoPermisoRepository tipoPermisoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Directorio local de almacenamiento
    private static final String UPLOAD_DIR = "uploads/permisos/";

    public WorkerController(PermisoRepository permisoRepository,
                            EmpleadoRepository empleadoRepository,
                            AsistenciaRepository asistenciaRepository,
                            TipoPermisoRepository tipoPermisoRepository,
                            BCryptPasswordEncoder passwordEncoder) {
        this.permisoRepository = permisoRepository;
        this.empleadoRepository = empleadoRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.tipoPermisoRepository = tipoPermisoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private Empleado getEmpleadoActual() {
        String dni = SecurityContextHolder.getContext().getAuthentication().getName();
        return empleadoRepository.findByDni(dni).orElse(null);
    }

    @GetMapping("/permisos")
    public String verPermisos(Model model) {
        Empleado emp = getEmpleadoActual();
        model.addAttribute("permisos", permisoRepository.findByEmpleado(emp));
        return "trabajador/permisos";
    }

    @PostMapping("/permisos/solicitar")
    public String solicitarPermiso(@RequestParam("tipoPermiso") String tipoPermisoNombre,
                                   @RequestParam("motivo") String motivo,
                                   @RequestParam(value = "archivo", required = false) MultipartFile archivo) {
        Empleado emp = getEmpleadoActual();
        if (emp != null) {
            // Mapeo dinámico al catálogo TIPO_PERMISO de MySQL
            TipoPermiso tp = tipoPermisoRepository.findByNombre(tipoPermisoNombre != null ? tipoPermisoNombre : "PERSONAL")
                    .orElseGet(() -> tipoPermisoRepository.save(new TipoPermiso(tipoPermisoNombre != null ? tipoPermisoNombre : "PERSONAL")));

            Permiso p = new Permiso();
            p.setEmpleado(emp);
            p.setTipoPermisoObj(tp);
            p.setMotivo(motivo);
            p.setEstado("PENDIENTE");

            if (archivo != null && !archivo.isEmpty()) {
                try {
                    File uploadDir = new File(UPLOAD_DIR);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    String fileName = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
                    Path filePath = Paths.get(UPLOAD_DIR + fileName);
                    Files.write(filePath, archivo.getBytes());

                    p.setComprobanteRuta(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            permisoRepository.save(p);
        }
        return "redirect:/trabajador/permisos";
    }

    @GetMapping("/perfil")
    public String verPerfil(Model model) {
        Empleado emp = getEmpleadoActual();
        model.addAttribute("empleado", emp);
        return "trabajador/perfil";
    }

    @PostMapping("/perfil/update")
    public String actualizarPassword(@RequestParam String newPassword) {
        Empleado emp = getEmpleadoActual();
        if (emp != null) {
            emp.setPassword(passwordEncoder.encode(newPassword));
            empleadoRepository.save(emp);
        }
        return "redirect:/trabajador/perfil?success";
    }

    @GetMapping("/historial")
    public String verHistorial(Model model) {
        Empleado emp = getEmpleadoActual();
        model.addAttribute("asistencias", asistenciaRepository.findByEmpleado(emp));
        return "trabajador/historial";
    }
}
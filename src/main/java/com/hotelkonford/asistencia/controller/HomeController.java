package com.hotelkonford.asistencia.controller;

import com.hotelkonford.asistencia.model.Empleado;
import com.hotelkonford.asistencia.repository.AsistenciaRepository;
import com.hotelkonford.asistencia.repository.EmpleadoRepository;
import com.hotelkonford.asistencia.repository.PermisoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class HomeController {

    private final EmpleadoRepository empleadoRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final PermisoRepository permisoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public HomeController(EmpleadoRepository empleadoRepository,
                          AsistenciaRepository asistenciaRepository,
                          PermisoRepository permisoRepository,
                          BCryptPasswordEncoder passwordEncoder) {
        this.empleadoRepository = empleadoRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.permisoRepository = permisoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (esAdmin) return "redirect:/admin/dashboard";
        return "redirect:/trabajador/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        String dni = SecurityContextHolder.getContext().getAuthentication().getName();
        Empleado emp = empleadoRepository.findByDni(dni).orElse(null);

        LocalDate hoy = LocalDate.now();
        var asistenciasHoy = asistenciaRepository.findByFecha(hoy);
        var todosEmpleados = empleadoRepository.findAll();

        model.addAttribute("totalEmpleados", todosEmpleados.size());
        model.addAttribute("presentes", asistenciasHoy.size());
        model.addAttribute("tardanzas", asistenciasHoy.stream()
                .filter(a -> "TARDANZA".equals(a.getEstado()))
                .count());
        model.addAttribute("faltas", Math.max(0, todosEmpleados.stream()
                .filter(e -> "TRABAJADOR".equalsIgnoreCase(e.getRol())).count() - asistenciasHoy.size()));

        List<String> alertas = new ArrayList<>();
        asistenciasHoy.stream()
            .filter(a -> "TARDANZA".equals(a.getEstado()))
            .forEach(a -> alertas.add(a.getEmpleado().getNombreCompleto() + " marcó entrada tarde hoy."));
        asistenciasHoy.stream()
            .filter(a -> "FUERA_DE_TURNO".equals(a.getEstado()))
            .forEach(a -> alertas.add(a.getEmpleado().getNombreCompleto() + " intentó marcar entrada fuera de su turno."));

        var dnisPresentes = asistenciasHoy.stream()
                .map(a -> a.getEmpleado().getDni()).collect(Collectors.toSet());
        todosEmpleados.stream()
            .filter(e -> "TRABAJADOR".equalsIgnoreCase(e.getRol()) && !dnisPresentes.contains(e.getDni()))
            .forEach(e -> alertas.add(e.getNombreCompleto() + " aún no ha registrado su entrada."));

        model.addAttribute("alertas", alertas);
        model.addAttribute("empleado", emp);

        var todas = asistenciaRepository.findAll();
        model.addAttribute("ultimasAsistencias", todas.size() > 5 ?
                todas.subList(todas.size() - 5, todas.size()) : todas);

        return "admin/dashboard";
    }

    @GetMapping("/trabajador/dashboard")
    public String workerDashboard(Model model) {
        String dni = SecurityContextHolder.getContext().getAuthentication().getName();
        Empleado emp = empleadoRepository.findByDni(dni).orElse(null);
        model.addAttribute("empleado", emp);
        return "trabajador/dashboard";
    }

    // Restablece la contraseña validando únicamente el documento
    @PostMapping("/recuperar-password")
    public String recuperarPassword(@RequestParam("dni") String dni,
                                    @RequestParam("newPassword") String newPassword,
                                    RedirectAttributes ra) {

        Optional<Empleado> empOpt = empleadoRepository.findByDni(dni.trim());

        if (empOpt.isEmpty()) {
            ra.addFlashAttribute("errorReset", "No se encontró ningún empleado con el documento ingresado.");
            return "redirect:/login";
        }

        Empleado emp = empOpt.get();
        emp.setPassword(passwordEncoder.encode(newPassword));
        empleadoRepository.save(emp);
        ra.addFlashAttribute("successReset", "¡Contraseña actualizada con éxito! Ya puedes iniciar sesión.");

        return "redirect:/login";
    }
}
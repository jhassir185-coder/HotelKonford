package com.hotelkonford.asistencia.controller;

import com.hotelkonford.asistencia.model.*;
import com.hotelkonford.asistencia.repository.*;
import com.hotelkonford.asistencia.service.AsistenciaService;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EmpleadoRepository empleadoRepository;
    private final HorarioRepository horarioRepository;
    private final PermisoRepository permisoRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final CargoRepository cargoRepository;
    private final AreaRepository areaRepository;
    private final RolRepository rolRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final AsistenciaService asistenciaService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminController(EmpleadoRepository empleadoRepository,
                           HorarioRepository horarioRepository,
                           PermisoRepository permisoRepository,
                           AsistenciaRepository asistenciaRepository,
                           CargoRepository cargoRepository,
                           AreaRepository areaRepository,
                           RolRepository rolRepository,
                           TipoDocumentoRepository tipoDocumentoRepository,
                           AsistenciaService asistenciaService,
                           BCryptPasswordEncoder passwordEncoder) {
        this.empleadoRepository = empleadoRepository;
        this.horarioRepository = horarioRepository;
        this.permisoRepository = permisoRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.cargoRepository = cargoRepository;
        this.areaRepository = areaRepository;
        this.rolRepository = rolRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.asistenciaService = asistenciaService;
        this.passwordEncoder = passwordEncoder;
    }

    // Permite al Administrador cambiar su contraseña desde su Dashboard
    @PostMapping("/cambiar-password")
    public String cambiarPasswordAdmin(@RequestParam("newPassword") String newPassword, RedirectAttributes ra) {
        String dni = SecurityContextHolder.getContext().getAuthentication().getName();
        Empleado admin = empleadoRepository.findByDni(dni).orElse(null);

        if (admin != null) {
            admin.setPassword(passwordEncoder.encode(newPassword));
            empleadoRepository.save(admin);
            ra.addFlashAttribute("success", "¡Tu contraseña de Administrador ha sido actualizada correctamente!");
        } else {
            ra.addFlashAttribute("error", "No se pudo actualizar la contraseña. Usuario no identificado.");
        }

        return "redirect:/admin/dashboard";
    }

    // MARCACIÓN POR DNI DESDE EL ADMIN
    @PostMapping("/asistencias/marcar-dni")
    public String marcarAsistenciaPorDni(@RequestParam("dni") String dni, RedirectAttributes ra) {
        String resultado = asistenciaService.registrarMarca(dni);

        if (resultado.contains("Empleado no encontrado")) {
            ra.addFlashAttribute("error", "No se encontró ningún empleado registrado con el N° de Documento: " + dni);
        } else if (resultado.contains("Ya registraste tu salida hoy")) {
            ra.addFlashAttribute("error", resultado);
        } else {
            ra.addFlashAttribute("success", resultado);
        }

        return "redirect:/admin/dashboard";
    }

    // GESTIÓN DE EMPLEADOS
    @GetMapping("/empleados")
    public String listarEmpleados(Model model) {
        model.addAttribute("empleados", empleadoRepository.findAll());
        model.addAttribute("horarios", horarioRepository.findAll());
        return "admin/empleados";
    }

    @PostMapping("/empleados/guardar")
    public String guardarEmpleado(@ModelAttribute Empleado empleado,
                                  @RequestParam(value = "tipoDocumento", required = false) String tipoDocNombre,
                                  @RequestParam(value = "cargo", required = false) String cargoNombre,
                                  @RequestParam(value = "area", required = false) String areaNombre,
                                  @RequestParam(value = "rol", required = false) String rolNombre,
                                  RedirectAttributes ra) {

        // Mapeo dinámico a entidades catálogo de MySQL
        TipoDocumento doc = tipoDocumentoRepository.findByNombre(tipoDocNombre != null ? tipoDocNombre : "DNI")
                .orElseGet(() -> tipoDocumentoRepository.save(new TipoDocumento(tipoDocNombre != null ? tipoDocNombre : "DNI")));

        Cargo cargo = cargoRepository.findByNombre(cargoNombre != null ? cargoNombre : "Recepcionista")
                .orElseGet(() -> cargoRepository.save(new Cargo(cargoNombre != null ? cargoNombre : "Recepcionista")));

        Area area = areaRepository.findByNombre(areaNombre != null ? areaNombre : "Recepción")
                .orElseGet(() -> areaRepository.save(new Area(areaNombre != null ? areaNombre : "Recepción")));

        Rol rol = rolRepository.findByNombre(rolNombre != null ? rolNombre : "TRABAJADOR")
                .orElseGet(() -> rolRepository.save(new Rol(rolNombre != null ? rolNombre : "TRABAJADOR")));

        if (empleado.getId() != null) {
            Empleado existente = empleadoRepository.findById(empleado.getId()).orElse(null);
            if (existente != null) {
                if (empleadoRepository.existsByDni(empleado.getDni()) &&
                    !existente.getDni().equals(empleado.getDni())) {
                    ra.addFlashAttribute("error", "Ya existe un empleado con el N° de Documento: " + empleado.getDni());
                    return "redirect:/admin/empleados";
                }
                existente.setTipoDocumentoObj(doc);
                existente.setDni(empleado.getDni());
                existente.setNombres(empleado.getNombres());
                existente.setApellidos(empleado.getApellidos());
                existente.setCargoObj(cargo);
                existente.setAreaObj(area);
                existente.setRolObj(rol);
                existente.setHorario(empleado.getHorario());

                if (empleado.getPassword() != null && !empleado.getPassword().trim().isEmpty()) {
                    existente.setPassword(passwordEncoder.encode(empleado.getPassword()));
                }
                empleadoRepository.save(existente);
                ra.addFlashAttribute("success", "Empleado actualizado correctamente.");
                return "redirect:/admin/empleados";
            }
        }

        if (empleadoRepository.existsByDni(empleado.getDni())) {
            ra.addFlashAttribute("error", "Ya existe un empleado con el N° de Documento: " + empleado.getDni());
            return "redirect:/admin/empleados";
        }

        empleado.setTipoDocumentoObj(doc);
        empleado.setCargoObj(cargo);
        empleado.setAreaObj(area);
        empleado.setRolObj(rol);
        empleado.setPassword(passwordEncoder.encode(empleado.getPassword()));
        empleadoRepository.save(empleado);

        ra.addFlashAttribute("success", "Empleado registrado correctamente.");
        return "redirect:/admin/empleados";
    }

    // ELIMINACIÓN SEGURA EN CASCADA CON IDs INTEGER
    @GetMapping("/empleados/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            String dniSesion = SecurityContextHolder.getContext().getAuthentication().getName();
            Empleado emp = empleadoRepository.findById(id).orElse(null);

            if (emp != null) {
                if (emp.getDni().equals(dniSesion)) {
                    ra.addFlashAttribute("error", "No puedes eliminar la cuenta de usuario con la que estás con la sesión activa.");
                    return "redirect:/admin/empleados";
                }

                List<Asistencia> asistencias = asistenciaRepository.findByEmpleado(emp);
                if (!asistencias.isEmpty()) {
                    asistenciaRepository.deleteAll(asistencias);
                }

                List<Permiso> permisos = permisoRepository.findByEmpleado(emp);
                if (!permisos.isEmpty()) {
                    permisoRepository.deleteAll(permisos);
                }

                empleadoRepository.delete(emp);
                ra.addFlashAttribute("success", "Empleado y su historial asociado fueron eliminados correctamente.");
            } else {
                ra.addFlashAttribute("error", "El empleado no existe o ya fue eliminado.");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Ocurrió un problema al eliminar el empleado: " + e.getMessage());
        }

        return "redirect:/admin/empleados";
    }

   // GESTIÓN DE HORARIOS
    @GetMapping("/horarios")
    public String listarHorarios(Model model) {
        model.addAttribute("horarios", horarioRepository.findAll());
        return "admin/horarios";
    }

    @PostMapping("/horarios/guardar")
    public String guardarHorario(@ModelAttribute Horario horario, RedirectAttributes ra) {
        // Solo valida que la hora de entrada y salida no sean idénticas
        if (horario.getHoraEntrada().equals(horario.getHoraSalida())) {
            ra.addFlashAttribute("error", "La hora de entrada y la hora de salida no pueden ser iguales.");
            return "redirect:/admin/horarios";
        }

        // Evita nombres de horarios duplicados
        if (horarioRepository.findAll().stream()
                .anyMatch(h -> h.getNombre().equalsIgnoreCase(horario.getNombre()))) {
            ra.addFlashAttribute("error", "Ya existe un horario registrado con ese nombre.");
            return "redirect:/admin/horarios";
        }

        horarioRepository.save(horario);
        ra.addFlashAttribute("success", "Horario guardado correctamente.");
        return "redirect:/admin/horarios";
    }

    @PostMapping("/horarios/eliminar/{id}")
    public String eliminarHorario(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            horarioRepository.deleteById(id);
            ra.addFlashAttribute("success", "Horario eliminado correctamente.");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "No se puede eliminar este horario porque hay empleados asignados a él. Cambia primero el horario de dichos empleados.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Ocurrió un error al eliminar el horario: " + e.getMessage());
        }
        return "redirect:/admin/horarios";
    }

    // GESTIÓN DE PERMISOS
    @GetMapping("/permisos")
    public String listarPermisos(Model model) {
        model.addAttribute("permisos", permisoRepository.findAll());
        return "admin/permisos";
    }

    @PostMapping("/permisos/responder")
    public String responderPermiso(@RequestParam Integer id,
                                    @RequestParam String accion,
                                    @RequestParam String comentario) {
        permisoRepository.findById(id).ifPresent(p -> {
            p.setEstado(accion);
            p.setComentarioJefa(comentario);
            permisoRepository.save(p);
        });
        return "redirect:/admin/permisos";
    }

    // HISTORIAL DE ASISTENCIAS GENERAL CON FILTRO DE FECHAS
    @GetMapping("/asistencias")
    public String listarAsistencias(@RequestParam(name = "fechaInicio", required = false) String fechaInicio,
                                    @RequestParam(name = "fechaFin", required = false) String fechaFin,
                                    Model model) {
        List<Asistencia> lista = asistenciaRepository.findAll();

        if (fechaInicio != null && !fechaInicio.trim().isEmpty() && fechaFin != null && !fechaFin.trim().isEmpty()) {
            try {
                LocalDate inicio = LocalDate.parse(fechaInicio.trim());
                LocalDate fin = LocalDate.parse(fechaFin.trim());
                lista = lista.stream()
                        .filter(a -> a.getFecha() != null && !a.getFecha().isBefore(inicio) && !a.getFecha().isAfter(fin))
                        .collect(Collectors.toList());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        model.addAttribute("asistencias", lista);
        model.addAttribute("empleadoFiltro", null);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        return "admin/asistencias";
    }

    @PostMapping("/asistencias/eliminar-historial")
    public String eliminarHistorial() {
        asistenciaRepository.deleteAll();
        return "redirect:/admin/asistencias";
    }

    // HISTORIAL DE ASISTENCIAS FILTRADO POR UN EMPLEADO
    @GetMapping("/asistencias/empleado/{id}")
    public String listarAsistenciasPorEmpleado(@PathVariable Integer id,
                                                @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
                                                @RequestParam(name = "fechaFin", required = false) String fechaFin,
                                                Model model) {
        Empleado emp = empleadoRepository.findById(id).orElse(null);
        List<Asistencia> lista = emp != null ? asistenciaRepository.findByEmpleado(emp) : asistenciaRepository.findAll();

        if (fechaInicio != null && !fechaInicio.trim().isEmpty() && fechaFin != null && !fechaFin.trim().isEmpty()) {
            try {
                LocalDate inicio = LocalDate.parse(fechaInicio.trim());
                LocalDate fin = LocalDate.parse(fechaFin.trim());
                lista = lista.stream()
                        .filter(a -> a.getFecha() != null && !a.getFecha().isBefore(inicio) && !a.getFecha().isAfter(fin))
                        .collect(Collectors.toList());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        model.addAttribute("asistencias", lista);
        model.addAttribute("empleadoFiltro", emp);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        return "admin/asistencias";
    }

    // EXPORTACIÓN A EXCEL (.XLSX) REAL
    @GetMapping("/asistencias/exportar-excel")
    public void exportarExcel(@RequestParam(name = "empleadoId", required = false) Integer empleadoId,
                              @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
                              @RequestParam(name = "fechaFin", required = false) String fechaFin,
                              HttpServletResponse response) throws IOException {

        List<Asistencia> lista;
        if (empleadoId != null) {
            Empleado emp = empleadoRepository.findById(empleadoId).orElse(null);
            lista = emp != null ? asistenciaRepository.findByEmpleado(emp) : asistenciaRepository.findAll();
        } else {
            lista = asistenciaRepository.findAll();
        }

        if (fechaInicio != null && !fechaInicio.trim().isEmpty() && fechaFin != null && !fechaFin.trim().isEmpty()) {
            try {
                LocalDate inicio = LocalDate.parse(fechaInicio.trim());
                LocalDate fin = LocalDate.parse(fechaFin.trim());
                lista = lista.stream()
                        .filter(a -> a.getFecha() != null && !a.getFecha().isBefore(inicio) && !a.getFecha().isAfter(fin))
                        .collect(Collectors.toList());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte_Asistencias");

        CellStyle headerStyle = workbook.createCellStyle();
        Font fontHeader = workbook.createFont();
        fontHeader.setBold(true);
        fontHeader.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(fontHeader);
        headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        String[] columnas = {"Fecha", "Documento", "Empleado", "Cargo", "Hora Entrada", "Hora Salida", "Estado", "Retraso (Min)"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(headerStyle);
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        int rowNum = 1;
        for (Asistencia a : lista) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(a.getFecha() != null ? a.getFecha().toString() : "");
            row.createCell(1).setCellValue(a.getEmpleado() != null ? a.getEmpleado().getDni() : "");
            row.createCell(2).setCellValue(a.getEmpleado() != null ? a.getEmpleado().getNombreCompleto() : "");
            row.createCell(3).setCellValue(a.getEmpleado() != null ? a.getEmpleado().getCargo() : "");
            row.createCell(4).setCellValue(a.getHoraIngreso() != null ? a.getHoraIngreso().format(timeFormatter) : "---");
            row.createCell(5).setCellValue(a.getHoraSalida() != null ? a.getHoraSalida().format(timeFormatter) : "---");
            row.createCell(6).setCellValue(a.getEstado() != null ? a.getEstado() : "");
            row.createCell(7).setCellValue(a.getMinutosTardanza() > 0 ? a.getMinutosTardanza() + " min" : "0 min");
        }

        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Reporte_Asistencias_HotelKonford.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
package com.hotelkonford.asistencia;

import com.hotelkonford.asistencia.model.*;
import com.hotelkonford.asistencia.repository.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EmpleadoRepository empleadoRepository;
    private final HorarioRepository horarioRepository;
    private final CargoRepository cargoRepository;
    private final AreaRepository areaRepository;
    private final RolRepository rolRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final TipoPermisoRepository tipoPermisoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(EmpleadoRepository empleadoRepository,
                           HorarioRepository horarioRepository,
                           CargoRepository cargoRepository,
                           AreaRepository areaRepository,
                           RolRepository rolRepository,
                           TipoDocumentoRepository tipoDocumentoRepository,
                           TipoPermisoRepository tipoPermisoRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.empleadoRepository = empleadoRepository;
        this.horarioRepository = horarioRepository;
        this.cargoRepository = cargoRepository;
        this.areaRepository = areaRepository;
        this.rolRepository = rolRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.tipoPermisoRepository = tipoPermisoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // 1. Inicializar Catálogo de Roles
        Rol rolAdmin = rolRepository.findByNombre("ADMIN")
                .orElseGet(() -> rolRepository.save(new Rol("ADMIN")));
        Rol rolTrabajador = rolRepository.findByNombre("TRABAJADOR")
                .orElseGet(() -> rolRepository.save(new Rol("TRABAJADOR")));

        // 2. Inicializar Catálogo de Tipos de Documento
        TipoDocumento docDni = tipoDocumentoRepository.findByNombre("DNI")
                .orElseGet(() -> tipoDocumentoRepository.save(new TipoDocumento("DNI")));
        tipoDocumentoRepository.findByNombre("CARNE_EXTRANJERIA")
                .orElseGet(() -> tipoDocumentoRepository.save(new TipoDocumento("CARNE_EXTRANJERIA")));
        tipoDocumentoRepository.findByNombre("PASAPORTE")
                .orElseGet(() -> tipoDocumentoRepository.save(new TipoDocumento("PASAPORTE")));

        // 3. Inicializar Catálogo de Áreas
        Area areaAdmin = areaRepository.findByNombre("Administración")
                .orElseGet(() -> areaRepository.save(new Area("Administración")));
        Area areaRecepcion = areaRepository.findByNombre("Recepción")
                .orElseGet(() -> areaRepository.save(new Area("Recepción")));
        areaRepository.findByNombre("Limpieza y Housekeeping")
                .orElseGet(() -> areaRepository.save(new Area("Limpieza y Housekeeping")));
        areaRepository.findByNombre("Mantenimiento")
                .orElseGet(() -> areaRepository.save(new Area("Mantenimiento")));
        areaRepository.findByNombre("Cocina y Restaurante")
                .orElseGet(() -> areaRepository.save(new Area("Cocina y Restaurante")));

        // 4. Inicializar Catálogo de Cargos
        Cargo cargoJefeRH = cargoRepository.findByNombre("Jefa de Recursos Humanos")
                .orElseGet(() -> cargoRepository.save(new Cargo("Jefa de Recursos Humanos")));
        Cargo cargoRecepcionista = cargoRepository.findByNombre("Recepcionista")
                .orElseGet(() -> cargoRepository.save(new Cargo("Recepcionista")));

        // 5. Inicializar Catálogo de Tipos de Permiso
        tipoPermisoRepository.findByNombre("PERSONAL")
                .orElseGet(() -> tipoPermisoRepository.save(new TipoPermiso("PERSONAL")));
        tipoPermisoRepository.findByNombre("SALUD")
                .orElseGet(() -> tipoPermisoRepository.save(new TipoPermiso("SALUD")));
        tipoPermisoRepository.findByNombre("MATERNIDAD")
                .orElseGet(() -> tipoPermisoRepository.save(new TipoPermiso("MATERNIDAD")));

        // 6. Inicializar Horario Por Defecto
        Horario h1;
        if (horarioRepository.count() == 0) {
            h1 = new Horario();
            h1.setNombre("Mañana");
            h1.setHoraEntrada(LocalTime.of(8, 0));
            h1.setHoraSalida(LocalTime.of(16, 0));
            h1.setTolerancia(15);
            h1 = horarioRepository.save(h1);
        } else {
            h1 = horarioRepository.findAll().get(0);
        }

        // 7. Inicializar Empleados Por Defecto
        if (empleadoRepository.count() == 0) {

            // Usuario Administrador
            Empleado admin = new Empleado();
            admin.setTipoDocumentoObj(docDni);
            admin.setDni("74581236");
            admin.setNombres("Maria");
            admin.setApellidos("Administradora");
            admin.setEmail("admin@hotelkonford.com");
            admin.setCargoObj(cargoJefeRH);
            admin.setAreaObj(areaAdmin);
            admin.setRolObj(rolAdmin);
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setHorario(h1);
            admin.setFechaIngreso(LocalDate.of(2023, 1, 15));
            admin.setActivo(true);
            empleadoRepository.save(admin);

            // Usuario Trabajador
            Empleado emp = new Empleado();
            emp.setTipoDocumentoObj(docDni);
            emp.setDni("12345678");
            emp.setNombres("Juan");
            emp.setApellidos("Pérez");
            emp.setEmail("juan.perez@hotelkonford.com");
            emp.setTelefono("987654321");
            emp.setCargoObj(cargoRecepcionista);
            emp.setAreaObj(areaRecepcion);
            emp.setRolObj(rolTrabajador);
            emp.setPassword(passwordEncoder.encode("123456"));
            emp.setHorario(h1);
            emp.setFechaIngreso(LocalDate.of(2024, 2, 10));
            emp.setActivo(true);
            empleadoRepository.save(emp);
        }
    }
}
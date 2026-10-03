package com.hotelkonford.asistencia.config;

import com.hotelkonford.asistencia.model.Empleado;
import com.hotelkonford.asistencia.repository.EmpleadoRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final EmpleadoRepository empleadoRepository;

    public AppUserDetailsService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
        Empleado emp = empleadoRepository.findByDni(dni)
                .orElseThrow(() -> new UsernameNotFoundException("Empleado no encontrado: " + dni));

        return new User(
                emp.getDni(),
                emp.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + emp.getRol()))
        );
    }
}
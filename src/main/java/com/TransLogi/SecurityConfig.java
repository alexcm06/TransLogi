/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Translogi;

/**
 *
 * @author sebas
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    //  Recursos estáticos y rutas del sistema que no requieren autenticación
    public static final String[] PUBLIC_URLS = {
        "/", "/index", "/js/**", "/css/**", "/webjars/**", "/login", "/error", "/acceso_denegado"
    };

    // Acciones completas de escritura (save/delete) sobre entidades maestras de administración
    public static final String[] ADMIN_URLS = {
        "/usuario/nuevo", "/usuario/guardar", "/usuario/modificar/**", "/usuario/eliminar/**",
        "/rol/nuevo", "/rol/guardar", "/rol/modificar/**",
        "/empresa/nuevo", "/empresa/guardar", "/empresa/modificar/**", "/empresa/eliminar/**"
    };

    //  Vistas de consulta general (get) y reportes de la operación logística
    public static final String[] ADMIN_OR_SUPERVISOR_URLS = {
        "/usuario/listado", "/rol/listado", "/empresa/listado",
        "/conductor/listado", "/ubicacion/listado", "/estadoViaje/listado",
        "/viaje/listado", "/viaje/reportes"
    };

    //  Gestión directa de la operación y asignaciones (Conductores, Viajes y Ubicaciones)
    public static final String[] USUARIO_URLS = {
        "/conductor/nuevo", "/conductor/guardar", "/conductor/modificar/**", "/conductor/eliminar/**",
        "/viaje/nuevo", "/viaje/guardar", "/viaje/modificar/**", "/viaje/eliminar/**",
        "/ubicacion/nuevo", "/ubicacion/guardar", "/ubicacion/modificar/**", "/ubicacion/eliminar/**",
        "/estadoViaje/nuevo", "/estadoViaje/guardar"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers(ADMIN_URLS).hasRole("ADMIN")
                .requestMatchers(ADMIN_OR_SUPERVISOR_URLS).hasAnyRole("ADMIN", "SUPERVISOR")
                .requestMatchers(USUARIO_URLS).hasRole("USUARIO")
                .anyRequest().authenticated()
        ).formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
        ).logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        ).exceptionHandling(exceptions -> exceptions
                .accessDeniedPage("/acceso_denegado")
        ).sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Este método será reemplazado la siguiente semana
    @Bean
    public UserDetailsService users(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("admin@translogi.com")
                .password(passwordEncoder.encode("123456"))
                .roles("ADMIN")
                .build();

        UserDetails supervisor = User.builder()
                .username("supervisor@translogi.com")
                .password(passwordEncoder.encode("123456"))
                .roles("SUPERVISOR")
                .build();

        UserDetails conductor = User.builder()
                .username("conductor@translogi.com")
                .password(passwordEncoder.encode("123456"))
                .roles("USUARIO")
                .build();

        return new InMemoryUserDetailsManager(admin, supervisor, conductor);
    }
}

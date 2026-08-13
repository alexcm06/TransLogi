package com.TransLogi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name = "conductor")
public class Conductor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conductor")
    private Integer idConductor;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "El nombre no puede estar vacio.")
    @Size(min = 2, max = 100, message = "El nombre no puede tener menos de 2 caracteres ni mas de 100 caracteres.")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "El nombre solo puede contener letras")
    private String nombre;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "El telefono no puede estar vacio.")
    @Pattern(regexp = "^[0-9]{4}-?[0-9]{4}$", message = "Debe tener 8 digitos, con guion opcional")
    private String telefono;

    @Column(name = "numero_licencia", nullable = false, unique = true, length = 50)
    @NotBlank(message = "El numero de licencia no puede estar vacio.")
    @Size(min = 6, max = 30, message = "El numero de licencia no puede tener menos de 6 caracteres ni mas de 30 caracteres.")
    private String numeroLicencia;

    @FutureOrPresent
    @Column(name = "fecha_vencimiento_licencia", nullable = false)
    @NotNull(message = "La fecha de vencimiento de la licencia es obligatoria.")
    private LocalDate fechaVencimientoLicencia;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "foto_licencia_frente", length = 1024)
    private String fotoLicenciaFrente;

    @Column(name = "foto_licencia_reverso", length = 1024)
    private String fotoLicenciaReverso;

    @OneToOne
    @JoinColumn(name = "id_usuario", unique = true)
    private Usuario usuario;
}

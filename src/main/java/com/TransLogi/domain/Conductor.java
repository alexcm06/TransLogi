/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.TransLogi.domain;

/**
 *
 * @author sebas
 */

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
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres.")
    private String nombre;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "El teléfono no puede estar vacío.")
    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres.")
    private String telefono;

    @Column(name = "numero_licencia", nullable = false, unique = true, length = 50)
    @NotBlank(message = "El número de licencia no puede estar vacío.")
    @Size(max = 50, message = "El número de licencia no puede tener más de 50 caracteres.")
    private String numeroLicencia;

    @Column(name = "fecha_vencimiento_licencia", nullable = false)
    @NotNull(message = "La fecha de vencimiento de la licencia es obligatoria.")
    private LocalDate fechaVencimientoLicencia;

    @Column(nullable = false)
    private boolean estado = true;

    @OneToOne
    @JoinColumn(name = "id_usuario", unique = true)
    private Usuario usuario;
}
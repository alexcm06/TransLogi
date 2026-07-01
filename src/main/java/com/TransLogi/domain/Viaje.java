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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Data;

@Data
@Entity
@Table(name = "viaje")
public class Viaje implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_viaje")
    private Integer idViaje;

    @Column(name = "fecha_programada", nullable = false)
    @NotNull(message = "La fecha programada es obligatoria.")
    private LocalDate fechaProgramada;

    @Column(name = "hora_programada", nullable = false)
    @NotNull(message = "La hora programada es obligatoria.")
    private LocalTime horaProgramada;

    @Column(name = "cantidad_pasajeros", nullable = false)
    @NotNull(message = "La cantidad de pasajeros es obligatoria.")
    @Min(value = 1, message = "Debe haber al menos 1 pasajero.")
    private Integer cantidadPasajeros = 1;

    @Column(name = "kilometros_recorridos", precision = 10, scale = 2)
    private BigDecimal kilometrosRecorridos;

    @Column(length = 500)
    @Size(max = 500, message = "Las observaciones no pueden superar 500 caracteres.")
    private String observaciones;

    @Column(name = "fecha_hora_inicio")
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    @ManyToOne
    @JoinColumn(name = "id_empresa", nullable = false)
    @NotNull(message = "Debe seleccionar una empresa.")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "id_conductor", nullable = false)
    @NotNull(message = "Debe asignar un conductor.")
    private Conductor conductor;

    @ManyToOne
    @JoinColumn(name = "id_origen", nullable = false)
    @NotNull(message = "Debe seleccionar un origen.")
    private Ubicacion origen;

    @ManyToOne
    @JoinColumn(name = "id_destino", nullable = false)
    @NotNull(message = "Debe seleccionar un destino.")
    private Ubicacion destino;

    @ManyToOne
    @JoinColumn(name = "id_estado_viaje", nullable = false)
    @NotNull(message = "Debe indicar el estado del viaje.")
    private EstadoViaje estadoViaje;
}

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
import lombok.Data;

@Data
@Entity
@Table(name = "estado_viaje")
public class EstadoViaje implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_viaje")
    private Integer idEstadoViaje;

    @Column(name = "nombre_estado", nullable = false, unique = true, length = 50)
    @NotBlank(message = "El nombre del estado no puede estar vacío.")
    @Size(max = 50, message = "El nombre del estado no puede tener más de 50 caracteres.")
    private String nombreEstado;
}

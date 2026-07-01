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
@Table(name = "ubicacion")
public class Ubicacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private Integer idUbicacion;

    @Column(nullable = false, length = 150)
    @NotBlank(message = "El nombre de la ubicación no puede estar vacío.")
    @Size(max = 150, message = "El nombre no puede tener más de 150 caracteres.")
    private String nombre;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "La dirección no puede estar vacía.")
    @Size(max = 255, message = "La dirección no puede tener más de 255 caracteres.")
    private String direccion;
}

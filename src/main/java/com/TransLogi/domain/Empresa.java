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
@Table(name = "empresa")
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Integer idEmpresa;

    @Column(nullable = false, length = 150)
    @NotBlank(message = "El nombre de la empresa no puede estar vacío.")
    @Size(max = 150, message = "El nombre no puede tener más de 150 caracteres.")
    private String nombre;

    @Column(length = 20)
    private String telefono;

    @Column(name = "correo_contacto", length = 150)
    @Email(message = "El correo de contacto no tiene un formato válido.")
    private String correoContacto;

    @Column(nullable = false)
    private boolean activo = true;
}

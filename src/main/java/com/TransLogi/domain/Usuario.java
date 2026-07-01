
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
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres.")
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    @NotBlank(message = "El correo no puede estar vacío.")
    @Email(message = "El correo no tiene un formato válido.")
    @Size(max = 150, message = "El correo no puede tener más de 150 caracteres.")
    private String correo;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "La contraseña no puede estar vacía.")
    private String contrasena;

    @Column(nullable = false)
    private boolean estado = true;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    @NotNull(message = "Debe seleccionar un rol.")
    private Rol rol;
}
package com.TransLogi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name = "gasto")
public class Gasto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gasto")
    private Integer idGasto;

    @Column(length = 255)
    @Size(max = 255, message = "La descripcion no puede superar 255 caracteres.")
    private String descripcion;

    @PositiveOrZero
    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "El monto es obligatorio.")
    private BigDecimal monto;

    @Column(nullable = false)
    @NotNull(message = "La fecha del gasto es obligatoria.")
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "id_viaje", nullable = false)
    @NotNull(message = "Debe asociar un viaje.")
    private Viaje viaje;

    @ManyToOne
    @JoinColumn(name = "id_tipo_gasto", nullable = false)
    @NotNull(message = "Debe seleccionar un tipo de gasto.")
    private TipoGasto tipoGasto;
}

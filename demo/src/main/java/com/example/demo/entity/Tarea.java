package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El campo nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El campo valor es obligatorio")
    @PositiveOrZero(message = "El valor debe ser mayor o igual a cero")
    private Double valor;
}

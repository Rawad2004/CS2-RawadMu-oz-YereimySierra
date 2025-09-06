// File: src/main/java/app/application/port/in/CreateProcedureCommand.java
package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateProcedureCommand(
        @NotBlank(message = "El nombre del procedimiento es obligatorio")
        String name,

        @NotNull(message = "El costo del procedimiento es obligatorio")
        @Positive(message = "El costo debe ser un valor positivo")
        BigDecimal cost
) {
    public CreateProcedureCommand {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del procedimiento no puede ser nulo o vacío");
        }
        if (cost == null) {
            throw new IllegalArgumentException("El costo no puede ser nulo");
        }
        if (cost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El costo debe ser mayor a cero");
        }
    }
}
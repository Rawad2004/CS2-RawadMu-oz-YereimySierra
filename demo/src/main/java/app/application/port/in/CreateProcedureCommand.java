// File: src/main/java/app/application/port/in/CreateProcedureCommand.java
package app.application.port.in;

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
) {}
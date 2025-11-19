
package app.application.port.in;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateProcedureCommand(
        @NotNull(message = "El ID del procedimiento es obligatorio")
        @Positive(message = "El ID del procedimiento debe ser positivo")
        Long procedureId,

        String name,

        BigDecimal cost
) {}
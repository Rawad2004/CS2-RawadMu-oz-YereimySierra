// File: src/main/java/app/application/port/in/UpdateMedicationCommand.java
package app.application.port.in;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateMedicationCommand(
        @NotNull(message = "El ID del medicamento es obligatorio")
        @Positive(message = "El ID del medicamento debe ser positivo")
        Long medicationId,

        String name,

        BigDecimal cost
) {}
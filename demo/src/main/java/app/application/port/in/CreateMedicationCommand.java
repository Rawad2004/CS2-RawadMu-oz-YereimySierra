package app.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateMedicationCommand(
        @NotBlank(message = "El nombre del medicamento es obligatorio")
        String name,

        @NotNull(message = "El costo es obligatorio")
        @Positive(message = "El costo debe ser positivo")
        BigDecimal cost
) {}

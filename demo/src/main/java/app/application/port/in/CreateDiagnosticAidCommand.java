// File: src/main/java/app/application/port/in/CreateDiagnosticAidCommand.java
package app.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateDiagnosticAidCommand(
        @NotBlank(message = "El nombre de la ayuda diagnóstica es obligatorio")
        String name,

        @NotNull(message = "El costo de la ayuda diagnóstica es obligatorio")
        @Positive(message = "El costo debe ser un valor positivo")
        BigDecimal cost
) {
    public CreateDiagnosticAidCommand {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ayuda diagnóstica no puede ser nulo o vacío");
        }
        if (cost == null) {
            throw new IllegalArgumentException("El costo no puede ser nulo");
        }
        if (cost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El costo debe ser mayor a cero");
        }
    }
}

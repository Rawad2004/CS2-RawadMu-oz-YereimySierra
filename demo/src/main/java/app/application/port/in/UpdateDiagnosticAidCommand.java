// File: src/main/java/app/application/port/in/UpdateDiagnosticAidCommand.java
package app.application.port.in;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateDiagnosticAidCommand(
        @NotNull(message = "El ID de la ayuda diagnóstica es obligatorio")
        @Positive(message = "El ID de la ayuda diagnóstica debe ser positivo")
        Long diagnosticAidId,

        String name,

        BigDecimal cost
) {}
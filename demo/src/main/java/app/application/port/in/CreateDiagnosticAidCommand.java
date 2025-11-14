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

}

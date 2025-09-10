// File: src/main/java/app/application/port/in/CreateMedicationCommand.java
package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateMedicationCommand(
        @NotBlank(message = "El nombre del medicamento es obligatorio")
        String name,

        @NotNull(message = "El costo del medicamento es obligatorio")
        @Positive(message = "El costo debe ser un valor positivo")
        BigDecimal cost
) {

}
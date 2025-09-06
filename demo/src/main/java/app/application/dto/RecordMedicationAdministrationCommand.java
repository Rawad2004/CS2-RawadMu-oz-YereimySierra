// File: src/main/java/app/application/dto/RecordMedicationAdministrationCommand.java
package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record RecordMedicationAdministrationCommand(
        @NotNull(message = "El ID del medicamento es obligatorio")
        @Positive(message = "El ID del medicamento debe ser positivo")
        Long medicationId,

        @NotBlank(message = "La dosis es obligatoria")
        String dose,

        @NotNull(message = "La hora de administración es obligatoria")
        LocalDateTime administrationTime
) {
    public RecordMedicationAdministrationCommand {
        if (dose == null || dose.trim().isEmpty()) {
            throw new IllegalArgumentException("La dosis no puede ser nula o vacía");
        }
        if (dose.length() > 50) {
            throw new IllegalArgumentException("La dosis no puede exceder 50 caracteres");
        }
        if (administrationTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La hora de administración no puede ser futura");
        }
    }
}
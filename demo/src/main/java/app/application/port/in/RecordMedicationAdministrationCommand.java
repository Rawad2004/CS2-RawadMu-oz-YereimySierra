// File: src/main/java/app/application/dto/RecordMedicationAdministrationCommand.java
package app.application.port.in;

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
) { }
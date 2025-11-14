// File: src/main/java/app/application/port/in/RecordProcedureAdministrationCommand.java
package app.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record RecordProcedureAdministrationCommand(
        @NotNull(message = "El ID del procedimiento es obligatorio")
        @Positive(message = "El ID del procedimiento debe ser positivo")
        Long procedureId,

        @NotNull(message = "La hora del procedimiento es obligatoria")
        LocalDateTime procedureTime,

        String observations
) {}
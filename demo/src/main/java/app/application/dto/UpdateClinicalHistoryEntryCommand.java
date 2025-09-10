// File: src/main/java/app/application/dto/UpdateClinicalHistoryEntryCommand.java
package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record UpdateClinicalHistoryEntryCommand(
        @NotBlank(message = "La cédula del paciente es obligatoria")
        String patientNationalId,

        @NotNull(message = "La fecha de la visita original es obligatoria")
        @PastOrPresent(message = "La fecha de la visita no puede ser futura")
        LocalDate originalVisitDate,

        @NotBlank(message = "El nuevo diagnóstico es obligatorio")
        String newDiagnosis,

        String additionalNotes,

        @NotNull(message = "La fecha de actualización es obligatoria")
        @PastOrPresent(message = "La fecha de actualización no puede ser futura")
        LocalDate updateDate
) { }
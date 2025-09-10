// File: src/main/java/app/application/port/in/UpdateDiagnosisCommand.java
package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record UpdateDiagnosisCommand(
        @NotBlank(message = "La cédula del paciente es obligatoria")
        String patientNationalId,

        @NotNull(message = "La fecha de la visita es obligatoria")
        @PastOrPresent(message = "La fecha de la visita no puede ser futura")
        LocalDate visitDate,

        @NotBlank(message = "El diagnóstico no puede estar vacío")
        String newDiagnosis
) { }

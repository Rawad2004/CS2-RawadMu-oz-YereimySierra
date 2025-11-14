// File: src/main/java/app/application/port/in/UpdateDiagnosisCommand.java
package app.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateDiagnosisCommand(
        @NotBlank(message = "La cédula del paciente es obligatoria")
        String patientNationalId,

        @NotNull(message = "La fecha de la visita es obligatoria")
        LocalDate visitDate,

        @NotBlank(message = "El nuevo diagnóstico es obligatorio")
        String newDiagnosis,

        String diagnosticResults,

        @NotNull(message = "La fecha de actualización es obligatoria")
        LocalDate updateDate
) {}
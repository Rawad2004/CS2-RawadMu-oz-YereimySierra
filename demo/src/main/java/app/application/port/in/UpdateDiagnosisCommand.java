// File: src/main/java/app/application/port/in/UpdateDiagnosisCommand.java
package app.application.port.in;

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
) {
    public UpdateDiagnosisCommand {
        if (patientNationalId == null || patientNationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        if (visitDate == null) {
            throw new IllegalArgumentException("La fecha de la visita es obligatoria");
        }
        if (visitDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de la visita no puede ser futura");
        }
        if (newDiagnosis == null || newDiagnosis.trim().isEmpty()) {
            throw new IllegalArgumentException("El diagnóstico no puede estar vacío");
        }
    }
}

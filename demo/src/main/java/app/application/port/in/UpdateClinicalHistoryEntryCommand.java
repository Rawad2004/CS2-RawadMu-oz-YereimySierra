// File: src/main/java/app/application/port/in/UpdateClinicalHistoryEntryCommand.java
package app.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateClinicalHistoryEntryCommand(
        @NotBlank(message = "La cédula del paciente es obligatoria")
        String patientNationalId,

        @NotNull(message = "La fecha de la visita original es obligatoria")
        LocalDate originalVisitDate,

        @NotBlank(message = "El nuevo diagnóstico es obligatorio")
        String newDiagnosis,

        String updateNotes,

        @NotNull(message = "La fecha de actualización es obligatoria")
        LocalDate updateDate
) {
    // Constructor alternativo sin updateNotes
    public UpdateClinicalHistoryEntryCommand(String patientNationalId, LocalDate originalVisitDate,
                                             String newDiagnosis, LocalDate updateDate) {
        this(patientNationalId, originalVisitDate, newDiagnosis, null, updateDate);
    }
}
// File: src/main/java/app/application/port/in/CreateClinicalHistoryEntryCommand.java
package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record CreateClinicalHistoryEntryCommand(
        @NotBlank(message = "La cédula del paciente es obligatoria")
        String patientNationalId,

        @NotBlank(message = "La cédula del médico es obligatoria")
        String doctorNationalId,

        @NotNull(message = "La fecha de la visita es obligatoria")
        @PastOrPresent(message = "La fecha de la visita no puede ser futura")
        LocalDate visitDate,

        @NotBlank(message = "El motivo de la consulta es obligatorio")
        String reasonForVisit,

        @NotBlank(message = "La sintomatología es obligatoria")
        String symptomatology,

        String diagnosis
) {
}
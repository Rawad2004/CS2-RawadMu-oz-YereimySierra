package app.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DeleteVisitCommand(

        @NotBlank(message = "La cédula del paciente es obligatoria")
        String patientNationalId,

        @NotNull(message = "La fecha de la visita es obligatoria")
        LocalDate visitDate
) {}

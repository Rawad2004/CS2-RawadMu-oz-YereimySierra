// File: src/main/java/app/application/port/in/CreateClinicalHistoryEntryCommand.java
package app.application.port.in;

import app.domain.model.ClinicalHistoryEntry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateClinicalHistoryEntryCommand(
        @NotBlank(message = "La cédula del paciente es obligatoria")
        String patientNationalId,

        @NotBlank(message = "La cédula del médico es obligatoria")
        String doctorNationalId,

        @NotNull(message = "La fecha de la visita es obligatoria")
        LocalDate visitDate,

        @NotBlank(message = "El motivo de la consulta es obligatorio")
        String reasonForVisit,

        @NotBlank(message = "La sintomatología es obligatoria")
        String symptomatology,

        String diagnosis, // Puede ser null inicialmente

        // Hacer más explícito que la orden es opcional
        String associatedOrderNumber,

        // Tipo de visita (opcional, se infiere si no se proporciona)
        ClinicalHistoryEntry.OrderType visitType
) {
    // Constructor alternativo para visitas sin orden
    public CreateClinicalHistoryEntryCommand {
        if (visitType == null) {
            visitType = (associatedOrderNumber != null && !associatedOrderNumber.trim().isEmpty())
                    ? ClinicalHistoryEntry.OrderType.DIAGNOSTIC_AID // Valor por defecto si hay orden
                    : ClinicalHistoryEntry.OrderType.NONE;
        }
    }
}
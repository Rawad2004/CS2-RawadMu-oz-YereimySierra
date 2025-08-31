package app.application.port.in;

import app.domain.model.Medication;

public interface CreateMedicationUseCase {
    Medication createMedication(CreateMedicationCommand command);
}
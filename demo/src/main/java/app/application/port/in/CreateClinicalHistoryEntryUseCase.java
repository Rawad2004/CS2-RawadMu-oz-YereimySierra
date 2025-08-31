package app.application.port.in;

import app.domain.model.ClinicalHistoryEntry;

public interface CreateClinicalHistoryEntryUseCase {
    ClinicalHistoryEntry createClinicalHistoryEntry(CreateClinicalHistoryEntryCommand command);
}
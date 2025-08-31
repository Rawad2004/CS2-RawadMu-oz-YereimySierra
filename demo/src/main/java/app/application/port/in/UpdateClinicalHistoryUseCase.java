// File: src/main/java/app/application/port/in/UpdateClinicalHistoryUseCase.java
package app.application.port.in;

import app.domain.model.ClinicalHistoryEntry;

public interface UpdateClinicalHistoryUseCase {
    ClinicalHistoryEntry updateDiagnosis(UpdateDiagnosisCommand command);
}
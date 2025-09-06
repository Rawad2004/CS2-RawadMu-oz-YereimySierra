// File: src/main/java/app/application/usecases/DoctorUseCases.java
package app.application.usecases;

import app.application.dto.CreateClinicalHistoryEntryCommand;
import app.application.dto.CreateOrderCommand;
import app.application.dto.UpdateClinicalHistoryEntryCommand;
import app.application.dto.UpdateDiagnosisCommand;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.Order;

public interface DoctorUseCases {

    interface CreateClinicalHistoryEntryUseCase {
        ClinicalHistoryEntry createClinicalHistoryEntry(CreateClinicalHistoryEntryCommand command);
    }

    interface UpdateClinicalHistoryEntryUseCase {
        ClinicalHistoryEntry updateClinicalHistoryEntry(UpdateClinicalHistoryEntryCommand command);
    }

    interface UpdateDiagnosisUseCase {
        ClinicalHistoryEntry updateDiagnosis(UpdateDiagnosisCommand command);
    }

    interface CreateOrderUseCase {
        Order createOrder(CreateOrderCommand command);
    }
}
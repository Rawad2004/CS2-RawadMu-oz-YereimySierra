// File: src/main/java/app/application/usecases/DoctorUseCases.java
package app.application.usecases;

import app.application.port.in.CreateClinicalHistoryEntryCommand;
import app.application.port.in.CreateOrderCommand;
import app.application.port.in.UpdateClinicalHistoryEntryCommand;
import app.application.port.in.UpdateDiagnosisCommand;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.Order;

import java.time.LocalDate;

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

    // NUEVOS USE CASES ESPECÍFICOS
    interface UpdateDiagnosisOnlyUseCase {
        ClinicalHistoryEntry updateDiagnosisOnly(String patientNationalId, LocalDate visitDate, String newDiagnosis);
    }

    interface UpdateDiagnosisAfterDiagnosticAidUseCase {
        ClinicalHistoryEntry updateDiagnosisAfterDiagnosticAid(String patientNationalId, LocalDate diagnosticVisitDate,
                                                               String diagnosis, String diagnosticResults);
    }

    interface AddVisitNotesUseCase {
        ClinicalHistoryEntry addVisitNotes(String patientNationalId, LocalDate visitDate, String additionalNotes);
    }

    interface CompleteVisitUseCase {
        ClinicalHistoryEntry completeVisit(String patientNationalId, LocalDate visitDate, String completionNotes);
    }
}
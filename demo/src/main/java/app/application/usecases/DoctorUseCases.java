package app.application.usecases;

import app.application.port.in.*;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    interface FindClinicalHistoryUseCase {
        Optional<ClinicalHistoryEntry> findByPatientNationalId(String patientNationalId);
    }


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

    interface DeleteVisitUseCase {
        void deleteVisit(String patientNationalId, LocalDate visitDate);
    }


    interface RegisterVisitUseCase {
        void registerVisit(String patientNationalId,
                           LocalDate visitDate,
                           RegisterVisitCommand command);
    }

    interface GetPatientOrdersUseCase {
        List<Order> getOrders(String patientNationalId);
    }

    interface GetPatientVisitsUseCase {
        List<VisitSummaryResponse> getVisits(String patientNationalId);
    }

    interface GetOrderDetailUseCase {
        Order getOrderByNumber(String orderNumber);
    }

    interface GetVisitDetailUseCase {
        VisitDetailResponse getVisitDetail(String patientNationalId,
                                           LocalDate visitDate);
    }

    interface UpdateVisitDiagnosisUseCase {
        void updateDiagnosis(UpdateDiagnosisCommand command);
    }

    interface AssociateOrderToVisitUseCase {
        void associateOrder(String patientNationalId,
                            LocalDate visitDate,
                            AssociateOrderToVisitCommand command);
    }

    interface MarkVisitPendingDiagnosticUseCase {
        void markPendingDiagnostic(String patientNationalId,
                                   LocalDate visitDate);
    }
}

// File: src/main/java/app/domain/repository/ClinicalHistoryRepositoryPort.java
package app.domain.repository;

import app.domain.model.ClinicalHistoryEntry;

import java.time.LocalDate;
import java.util.Optional;

public interface ClinicalHistoryRepositoryPort {

    ClinicalHistoryEntry save(ClinicalHistoryEntry entry);

    Optional<ClinicalHistoryEntry> findByPatientNationalId(String patientNationalId);

    Optional<ClinicalHistoryEntry.VisitData> findVisitByPatientAndDate(String patientNationalId, LocalDate visitDate);

    ClinicalHistoryEntry updateDiagnosis(String patientNationalId, LocalDate visitDate,
                                         String newDiagnosis, String updateNotes);

    ClinicalHistoryEntry addVisit(String patientNationalId, LocalDate visitDate,
                                  String doctorNationalId, String reasonForVisit,
                                  String symptomatology, String diagnosis, String orderNumber,
                                  ClinicalHistoryEntry.OrderType orderType);

    // NUEVO: Método para asociar orden a visita existente
    ClinicalHistoryEntry associateOrderToVisit(String patientNationalId, LocalDate visitDate,
                                               String orderNumber, ClinicalHistoryEntry.OrderType orderType);

    // NUEVO: Método para marcar como pendiente de diagnóstico
    ClinicalHistoryEntry markVisitAsPendingDiagnostic(String patientNationalId, LocalDate visitDate);

    boolean existsVisit(String patientNationalId, LocalDate visitDate);

    void deleteVisit(String patientNationalId, LocalDate visitDate);
}
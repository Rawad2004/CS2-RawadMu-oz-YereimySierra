package app.domain.repository;

import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.Visit;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClinicalHistoryRepositoryPort {

    ClinicalHistoryEntry save(ClinicalHistoryEntry entry);

    Optional<ClinicalHistoryEntry> findByPatientNationalId(String patientNationalId);

    Optional<ClinicalHistoryEntry.VisitData> findVisitByPatientAndDate(String patientNationalId,
                                                                       LocalDate visitDate);

    ClinicalHistoryEntry updateDiagnosis(String patientNationalId, LocalDate visitDate,
                                         String newDiagnosis, String updateNotes);

    ClinicalHistoryEntry addVisit(String patientNationalId, LocalDate visitDate,
                                  String doctorNationalId, String reasonForVisit,
                                  String symptomatology, String diagnosis,
                                  String orderNumber, ClinicalHistoryEntry.OrderType orderType);

    ClinicalHistoryEntry associateOrderToVisit(String patientNationalId, LocalDate visitDate,
                                               String orderNumber, ClinicalHistoryEntry.OrderType orderType);

    ClinicalHistoryEntry markVisitAsPendingDiagnostic(String patientNationalId, LocalDate visitDate);

    boolean existsVisit(String patientNationalId, LocalDate visitDate);

    void deleteVisit(String patientNationalId, LocalDate visitDate);

    List<Visit> findVisits(String patientNationalId);

    void createVisit(String patientNationalId,
                     LocalDate visitDate,
                     String reason,
                     String notes);

    ClinicalHistoryEntry registerVitalSigns(String patientNationalId,
                                            LocalDate visitDate,
                                            String bloodPressure,
                                            Double temperature,
                                            Integer pulse,
                                            Integer oxygenSaturation,
                                            String nurseNotes);

    // 👇 NUEVO: intervenciones de enfermería
    ClinicalHistoryEntry addNursingRecord(String patientNationalId,
                                          LocalDate visitDate,
                                          String orderNumber,
                                          int itemNumber,
                                          String description);
}

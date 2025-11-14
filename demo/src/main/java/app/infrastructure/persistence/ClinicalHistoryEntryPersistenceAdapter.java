// File: src/main/java/app/infrastructure/persistence/ClinicalHistoryEntryPersistenceAdapter.java
package app.infrastructure.persistence;

import app.domain.model.ClinicalHistoryEntry;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import app.infrastructure.persistence.mongodb.ClinicalHistoryMongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public class ClinicalHistoryEntryPersistenceAdapter implements ClinicalHistoryRepositoryPort {

    private final ClinicalHistoryMongoRepository clinicalHistoryMongoRepository;

    public ClinicalHistoryEntryPersistenceAdapter(ClinicalHistoryMongoRepository clinicalHistoryMongoRepository) {
        this.clinicalHistoryMongoRepository = clinicalHistoryMongoRepository;
    }

    @Override
    public ClinicalHistoryEntry save(ClinicalHistoryEntry entry) {
        return clinicalHistoryMongoRepository.save(entry);
    }

    @Override
    public Optional<ClinicalHistoryEntry> findByPatientNationalId(String patientNationalId) {
        return clinicalHistoryMongoRepository.findByPatientNationalId(patientNationalId);
    }

    @Override
    public Optional<ClinicalHistoryEntry.VisitData> findVisitByPatientAndDate(String patientNationalId, LocalDate visitDate) {
        return clinicalHistoryMongoRepository.findByPatientNationalId(patientNationalId)
                .map(entry -> entry.getVisit(visitDate));
    }

    @Override
    public ClinicalHistoryEntry updateDiagnosis(String patientNationalId, LocalDate visitDate,
                                                String newDiagnosis, String updateNotes) {
        ClinicalHistoryEntry entry = clinicalHistoryMongoRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException("Historia clínica no encontrada para el paciente: " + patientNationalId));

        entry.updateDiagnosis(visitDate, newDiagnosis, updateNotes);
        return clinicalHistoryMongoRepository.save(entry);
    }

    @Override
    public ClinicalHistoryEntry addVisit(String patientNationalId, LocalDate visitDate,
                                         String doctorNationalId, String reasonForVisit,
                                         String symptomatology, String diagnosis, String orderNumber,
                                         ClinicalHistoryEntry.OrderType orderType) {

        ClinicalHistoryEntry entry = clinicalHistoryMongoRepository.findByPatientNationalId(patientNationalId)
                .orElse(new ClinicalHistoryEntry(patientNationalId));

        entry.addVisit(visitDate, doctorNationalId, reasonForVisit, symptomatology, diagnosis, orderNumber, orderType);
        return clinicalHistoryMongoRepository.save(entry);
    }

    @Override
    public ClinicalHistoryEntry associateOrderToVisit(String patientNationalId, LocalDate visitDate,
                                                      String orderNumber, ClinicalHistoryEntry.OrderType orderType) {
        ClinicalHistoryEntry entry = clinicalHistoryMongoRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException("Historia clínica no encontrada para el paciente: " + patientNationalId));

        entry.associateOrder(visitDate, orderNumber, orderType);
        return clinicalHistoryMongoRepository.save(entry);
    }

    @Override
    public ClinicalHistoryEntry markVisitAsPendingDiagnostic(String patientNationalId, LocalDate visitDate) {
        ClinicalHistoryEntry entry = clinicalHistoryMongoRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException("Historia clínica no encontrada para el paciente: " + patientNationalId));

        entry.markAsPendingDiagnostic(visitDate);
        return clinicalHistoryMongoRepository.save(entry);
    }

    @Override
    public boolean existsVisit(String patientNationalId, LocalDate visitDate) {
        return clinicalHistoryMongoRepository.findByPatientNationalId(patientNationalId)
                .map(entry -> entry.hasVisitOnDate(visitDate))
                .orElse(false);
    }

    @Override
    public void deleteVisit(String patientNationalId, LocalDate visitDate) {
        ClinicalHistoryEntry entry = clinicalHistoryMongoRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException("Historia clínica no encontrada"));

        if (entry.getVisitData() != null) {
            entry.getVisitData().remove(visitDate);
            clinicalHistoryMongoRepository.save(entry);
        }
    }
}
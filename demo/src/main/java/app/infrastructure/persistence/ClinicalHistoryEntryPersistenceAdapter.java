// File: src/main/java/app/infrastructure/persistence/ClinicalHistoryEntryPersistenceAdapter.java
package app.infrastructure.persistence;

import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.vo.NationalId;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import app.infrastructure.persistence.jpa.ClinicalHistoryEntryJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ClinicalHistoryEntryPersistenceAdapter implements ClinicalHistoryRepositoryPort {

    private final ClinicalHistoryEntryJpaRepository clinicalHistoryRepository;

    public ClinicalHistoryEntryPersistenceAdapter(ClinicalHistoryEntryJpaRepository clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public ClinicalHistoryEntry save(ClinicalHistoryEntry entry) {
        return clinicalHistoryRepository.save(entry);
    }

    @Override
    public List<ClinicalHistoryEntry> findByPatientId(NationalId patientId) {
        // Como patientId es un @Embedded con "value", hay que usar el sufijo "_Value"
        return clinicalHistoryRepository.findByPatientId_Value(patientId.getValue());
    }

    @Override
    public Optional<ClinicalHistoryEntry> findByPatientIdAndVisitDate(NationalId patientId, LocalDate visitDate) {
        return clinicalHistoryRepository.findByPatientId_ValueAndVisitDate(patientId.getValue(), visitDate);
    }

    @Override
    public ClinicalHistoryEntry updateDiagnosis(Long entryId, String newDiagnosis, LocalDate updateDate) {
        ClinicalHistoryEntry entry = clinicalHistoryRepository.findById(entryId)
                .orElseThrow(() -> new IllegalArgumentException("Entrada no encontrada con id: " + entryId));

        entry.updateDiagnosisWithNotes(newDiagnosis, null, updateDate);
        return clinicalHistoryRepository.save(entry);
    }

    @Override
    public ClinicalHistoryEntry updateEntry(Long entryId, String newDiagnosis, String additionalNotes, LocalDate updateDate) {
        ClinicalHistoryEntry entry = clinicalHistoryRepository.findById(entryId)
                .orElseThrow(() -> new IllegalArgumentException("Entrada no encontrada con id: " + entryId));

        entry.updateDiagnosisWithNotes(newDiagnosis, additionalNotes, updateDate);
        return clinicalHistoryRepository.save(entry);
    }

    @Override
    public Optional<ClinicalHistoryEntry> findById(Long entryId) {
        return clinicalHistoryRepository.findById(entryId);
    }

    @Override
    public void deleteById(Long entryId) {
        clinicalHistoryRepository.deleteById(entryId);
    }
}

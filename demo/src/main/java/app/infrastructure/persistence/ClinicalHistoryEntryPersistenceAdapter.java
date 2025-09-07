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
        return clinicalHistoryRepository.findByPatientId(patientId);
    }

    @Override
    public Optional<ClinicalHistoryEntry> findByPatientIdAndVisitDate(NationalId patientId, LocalDate visitDate) {
        return clinicalHistoryRepository.findByPatientIdAndVisitDate(patientId, visitDate);
    }

    @Override
    public ClinicalHistoryEntry updateDiagnosis(Long entryId, String newDiagnosis, LocalDate updateDate) {
        return null;
    }

    @Override
    public ClinicalHistoryEntry updateEntry(Long entryId, String newDiagnosis, String additionalNotes, LocalDate updateDate) {
        return null;
    }

    @Override
    public Optional<ClinicalHistoryEntry> findById(Long entryId) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long entryId) {

    }
}
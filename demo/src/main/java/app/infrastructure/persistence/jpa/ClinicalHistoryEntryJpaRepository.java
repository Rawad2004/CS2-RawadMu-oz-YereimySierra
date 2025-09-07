package app.infrastructure.persistence.jpa;

import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.vo.NationalId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClinicalHistoryEntryJpaRepository extends JpaRepository<ClinicalHistoryEntry, String> {
    List<ClinicalHistoryEntry> findByPatientId(NationalId patientId);
    Optional<ClinicalHistoryEntry> findByPatientIdAndVisitDate(NationalId patientId, LocalDate visitDate);
}
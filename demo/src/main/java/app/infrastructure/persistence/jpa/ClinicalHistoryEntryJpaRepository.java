package app.infrastructure.persistence.jpa;

import app.domain.model.ClinicalHistoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClinicalHistoryEntryJpaRepository extends JpaRepository<ClinicalHistoryEntry, Long> {

    List<ClinicalHistoryEntry> findByPatientId_Value(String patientNationalId);

    Optional<ClinicalHistoryEntry> findByPatientId_ValueAndVisitDate(String patientNationalId, LocalDate visitDate);
}

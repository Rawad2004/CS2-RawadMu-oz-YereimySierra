package app.domain.repository;

import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.vo.NationalId;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClinicalHistoryRepositoryPort {

    ClinicalHistoryEntry save(ClinicalHistoryEntry entry);

    /**
     * Busca toda la historia clínica de un paciente.
     * @param patientId La cédula del paciente.
     * @return Una lista de todas las entradas de su historia.
     */
    List<ClinicalHistoryEntry> findByPatientId(NationalId patientId);

    /**
     * Busca una entrada específica de la historia por paciente y fecha.
     * @param patientId La cédula del paciente.
     * @param visitDate La fecha de la visita.
     * @return Un Optional con la entrada si se encuentra.
     */
    Optional<ClinicalHistoryEntry> findByPatientIdAndVisitDate(NationalId patientId, LocalDate visitDate);
}
// File: src/main/java/app/domain/repository/ClinicalHistoryRepositoryPort.java
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

    /**
     * Actualiza el diagnóstico de una entrada de historia clínica.
     * @param entryId ID de la entrada a actualizar.
     * @param newDiagnosis Nuevo diagnóstico.
     * @param updateDate Fecha de la actualización.
     * @return La entrada actualizada.
     */
    ClinicalHistoryEntry updateDiagnosis(Long entryId, String newDiagnosis, LocalDate updateDate);

    /**
     * Actualiza múltiples campos de una entrada de historia clínica.
     * @param entryId ID de la entrada a actualizar.
     * @param newDiagnosis Nuevo diagnóstico (opcional).
     * @param additionalNotes Notas adicionales (opcional).
     * @param updateDate Fecha de la actualización.
     * @return La entrada actualizada.
     */
    ClinicalHistoryEntry updateEntry(Long entryId, String newDiagnosis, String additionalNotes, LocalDate updateDate);

    /**
     * Busca una entrada por su ID.
     * @param entryId ID de la entrada.
     * @return Un Optional con la entrada si se encuentra.
     */
    Optional<ClinicalHistoryEntry> findById(Long entryId);

    /**
     * Elimina una entrada de historia clínica.
     * @param entryId ID de la entrada a eliminar.
     */
    void deleteById(Long entryId);
}
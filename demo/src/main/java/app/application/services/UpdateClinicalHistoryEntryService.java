// File: src/main/java/app/application/services/UpdateClinicalHistoryEntryService.java
package app.application.services;

import app.application.dto.UpdateClinicalHistoryEntryCommand;
import app.application.usecases.DoctorUseCases;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.vo.NationalId;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateClinicalHistoryEntryService implements DoctorUseCases.UpdateClinicalHistoryEntryUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public UpdateClinicalHistoryEntryService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public ClinicalHistoryEntry updateClinicalHistoryEntry(UpdateClinicalHistoryEntryCommand command) {
        // Buscar la entrada original de historia clínica
        ClinicalHistoryEntry existingEntry = clinicalHistoryRepository
                .findByPatientIdAndVisitDate(
                        new NationalId(command.patientNationalId()),
                        command.originalVisitDate()
                )
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró entrada de historia clínica para el paciente: " +
                                command.patientNationalId() + " en la fecha: " + command.originalVisitDate()
                ));

        // Validar que la actualización sea necesaria
        if (existingEntry.getDiagnosis().equals(command.newDiagnosis()) &&
                (command.additionalNotes() == null || command.additionalNotes().isEmpty())) {
            throw new IllegalStateException("No hay cambios para actualizar en la entrada de historia clínica");
        }

        ClinicalHistoryEntry updatedEntry = clinicalHistoryRepository.updateEntry(
                Long.parseLong(existingEntry.getId()), // Asumiendo que el ID es Long
                command.newDiagnosis(),
                command.additionalNotes(),
                command.updateDate()
        );

        return updatedEntry;
    }

    public ClinicalHistoryEntry updateDiagnosisOnly(UpdateClinicalHistoryEntryCommand command) {
        ClinicalHistoryEntry existingEntry = clinicalHistoryRepository
                .findByPatientIdAndVisitDate(
                        new NationalId(command.patientNationalId()),
                        command.originalVisitDate()
                )
                .orElseThrow(() -> new IllegalArgumentException(
                        "Entrada de historia clínica no encontrada"
                ));

        return clinicalHistoryRepository.updateDiagnosis(
                Long.parseLong(existingEntry.getId()),
                command.newDiagnosis(),
                command.updateDate()
        );
    }
}
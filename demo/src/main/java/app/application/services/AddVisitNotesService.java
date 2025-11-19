package app.application.services;

import app.application.usecases.DoctorUseCases.AddVisitNotesUseCase;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class AddVisitNotesService implements AddVisitNotesUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public AddVisitNotesService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public ClinicalHistoryEntry addVisitNotes(String patientNationalId,
                                              LocalDate visitDate,
                                              String additionalNotes) {
        if (patientNationalId == null || patientNationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        if (visitDate == null) {
            throw new IllegalArgumentException("La fecha de la visita es obligatoria");
        }
        if (additionalNotes == null || additionalNotes.trim().isEmpty()) {
            throw new IllegalArgumentException("Las notas adicionales son obligatorias");
        }

        var visitOpt = clinicalHistoryRepository.findVisitByPatientAndDate(patientNationalId, visitDate);
        if (visitOpt.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe visita para el paciente " + patientNationalId +
                            " en la fecha " + visitDate
            );
        }

        ClinicalHistoryEntry.VisitData visit = visitOpt.get();
        String currentDiagnosis = visit.getDiagnosis();
        if (currentDiagnosis == null || currentDiagnosis.trim().isEmpty()) {
            currentDiagnosis = "SIN_DIAGNOSTICO_DEFINIDO";
        }

        String currentNotes = visit.getUpdateNotes();
        String newNotes;
        if (currentNotes == null || currentNotes.isBlank()) {
            newNotes = additionalNotes;
        } else {
            newNotes = currentNotes + System.lineSeparator() + additionalNotes;
        }

        return clinicalHistoryRepository.updateDiagnosis(
                patientNationalId,
                visitDate,
                currentDiagnosis,
                newNotes
        );
    }
}

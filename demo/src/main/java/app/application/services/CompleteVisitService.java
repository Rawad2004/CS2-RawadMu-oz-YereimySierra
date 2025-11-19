package app.application.services;

import app.application.usecases.DoctorUseCases.CompleteVisitUseCase;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class CompleteVisitService implements CompleteVisitUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public CompleteVisitService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public ClinicalHistoryEntry completeVisit(String patientNationalId,
                                              LocalDate visitDate,
                                              String completionNotes) {
        if (patientNationalId == null || patientNationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        if (visitDate == null) {
            throw new IllegalArgumentException("La fecha de la visita es obligatoria");
        }
        if (completionNotes == null || completionNotes.trim().isEmpty()) {
            throw new IllegalArgumentException("Las notas de cierre de la visita son obligatorias");
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
        String completionLine = "Visita cerrada: " + completionNotes;

        String newNotes;
        if (currentNotes == null || currentNotes.isBlank()) {
            newNotes = completionLine;
        } else {
            newNotes = currentNotes + System.lineSeparator() + completionLine;
        }

        return clinicalHistoryRepository.updateDiagnosis(
                patientNationalId,
                visitDate,
                currentDiagnosis,
                newNotes
        );
    }
}

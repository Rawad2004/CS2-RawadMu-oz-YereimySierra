package app.application.services;

import app.application.usecases.DoctorUseCases.UpdateDiagnosisOnlyUseCase;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class UpdateDiagnosisOnlyService implements UpdateDiagnosisOnlyUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public UpdateDiagnosisOnlyService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public ClinicalHistoryEntry updateDiagnosisOnly(String patientNationalId,
                                                    LocalDate visitDate,
                                                    String newDiagnosis) {
        if (patientNationalId == null || patientNationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        if (visitDate == null) {
            throw new IllegalArgumentException("La fecha de la visita es obligatoria");
        }
        if (newDiagnosis == null || newDiagnosis.trim().isEmpty()) {
            throw new IllegalArgumentException("El diagnóstico es obligatorio");
        }

        boolean exists = clinicalHistoryRepository.existsVisit(patientNationalId, visitDate);
        if (!exists) {
            throw new ResourceNotFoundException(
                    "No existe visita para el paciente " + patientNationalId + " en la fecha " + visitDate
            );
        }

        String notes = "Actualización de diagnóstico sin ayuda diagnóstica.";
        return clinicalHistoryRepository.updateDiagnosis(patientNationalId, visitDate, newDiagnosis, notes);
    }
}

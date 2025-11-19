package app.application.services;

import app.application.usecases.DoctorUseCases.UpdateDiagnosisAfterDiagnosticAidUseCase;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class UpdateDiagnosisAfterDiagnosticAidService
        implements UpdateDiagnosisAfterDiagnosticAidUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public UpdateDiagnosisAfterDiagnosticAidService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public ClinicalHistoryEntry updateDiagnosisAfterDiagnosticAid(
            String patientNationalId,
            LocalDate diagnosticVisitDate,
            String diagnosis,
            String diagnosticResults
    ) {
        if (patientNationalId == null || patientNationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        if (diagnosticVisitDate == null) {
            throw new IllegalArgumentException("La fecha de la visita es obligatoria");
        }
        if (diagnosis == null || diagnosis.trim().isEmpty()) {
            throw new IllegalArgumentException("El diagnóstico es obligatorio");
        }
        if (diagnosticResults == null || diagnosticResults.trim().isEmpty()) {
            throw new IllegalArgumentException("Los resultados de la ayuda diagnóstica son obligatorios");
        }

        boolean exists = clinicalHistoryRepository.existsVisit(patientNationalId, diagnosticVisitDate);
        if (!exists) {
            throw new ResourceNotFoundException(
                    "No existe visita para el paciente " + patientNationalId +
                            " en la fecha " + diagnosticVisitDate
            );
        }

        String notes = "Actualización de diagnóstico posterior a ayuda diagnóstica. Resultados: "
                + diagnosticResults;

        return clinicalHistoryRepository.updateDiagnosis(
                patientNationalId,
                diagnosticVisitDate,
                diagnosis,
                notes
        );
    }
}

package app.application.services;

import app.application.usecases.DoctorUseCases;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class MarkVisitPendingDiagnosticService implements DoctorUseCases.MarkVisitPendingDiagnosticUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public MarkVisitPendingDiagnosticService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public void markPendingDiagnostic(String patientNationalId, LocalDate visitDate) {
        if (patientNationalId == null || patientNationalId.isBlank()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        if (visitDate == null) {
            throw new IllegalArgumentException("La fecha de la visita es obligatoria");
        }

        clinicalHistoryRepository.markVisitAsPendingDiagnostic(patientNationalId, visitDate);
    }
}

// File: src/main/java/app/application/services/DeleteVisitService.java
package app.application.services;

import app.application.usecases.DoctorUseCases.DeleteVisitUseCase;
import app.domain.exception.ResourceNotFoundException;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class DeleteVisitService implements DeleteVisitUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public DeleteVisitService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public void deleteVisit(String patientNationalId, LocalDate visitDate) {
        if (patientNationalId == null || patientNationalId.isBlank()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }

        if (visitDate == null) {
            throw new IllegalArgumentException("La fecha de la visita es obligatoria");
        }

        boolean exists = clinicalHistoryRepository.existsVisit(patientNationalId, visitDate);
        if (!exists) {
            throw new ResourceNotFoundException(
                    "No existe visita para el paciente " + patientNationalId +
                            " en la fecha " + visitDate
            );
        }

        clinicalHistoryRepository.deleteVisit(patientNationalId, visitDate);
    }
}

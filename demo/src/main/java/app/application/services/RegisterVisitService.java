package app.application.services;

import app.application.port.in.RegisterVisitCommand;
import app.application.usecases.DoctorUseCases;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class RegisterVisitService implements DoctorUseCases.RegisterVisitUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public RegisterVisitService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public void registerVisit(String patientNationalId,
                              LocalDate visitDate,
                              RegisterVisitCommand command) {

        if (patientNationalId == null || patientNationalId.isBlank()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        if (visitDate == null) {
            throw new IllegalArgumentException("La fecha de la visita es obligatoria");
        }

        String reason = command != null ? command.reason() : null;
        String notes = command != null ? command.notes() : null;

        clinicalHistoryRepository.createVisit(
                patientNationalId,
                visitDate,
                reason,
                notes
        );
    }
}

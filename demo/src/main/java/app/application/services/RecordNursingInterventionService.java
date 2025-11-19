package app.application.services;

import app.application.port.in.RecordNursingInterventionCommand;
import app.application.usecases.NurseUseCases;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class RecordNursingInterventionService implements NurseUseCases.RecordNursingInterventionUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public RecordNursingInterventionService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public void recordNursingIntervention(String patientNationalId,
                                          LocalDate visitDate,
                                          RecordNursingInterventionCommand command) {

        if (patientNationalId == null || patientNationalId.isBlank()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        if (visitDate == null) {
            throw new IllegalArgumentException("La fecha de la visita es obligatoria");
        }
        if (command == null) {
            throw new IllegalArgumentException("Los datos de la intervención son obligatorios");
        }

        clinicalHistoryRepository.addNursingRecord(
                patientNationalId,
                visitDate,
                command.orderNumber(),
                command.itemNumber(),
                command.description()
        );
    }
}

package app.application.services;

import app.application.port.in.UpdateDiagnosisCommand;
import app.application.usecases.DoctorUseCases;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateVisitDiagnosisService implements DoctorUseCases.UpdateVisitDiagnosisUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public UpdateVisitDiagnosisService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public void updateDiagnosis(UpdateDiagnosisCommand command) {

        if (command == null) {
            throw new IllegalArgumentException("Los datos de actualización de diagnóstico son obligatorios");
        }
        if (command.patientNationalId() == null || command.patientNationalId().isBlank()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        if (command.visitDate() == null) {
            throw new IllegalArgumentException("La fecha de la visita es obligatoria");
        }
        if (command.newDiagnosis() == null || command.newDiagnosis().isBlank()) {
            throw new IllegalArgumentException("El nuevo diagnóstico es obligatorio");
        }

        clinicalHistoryRepository.updateDiagnosis(
                command.patientNationalId(),
                command.visitDate(),
                command.newDiagnosis(),
                command.diagnosticResults()
        );
    }
}

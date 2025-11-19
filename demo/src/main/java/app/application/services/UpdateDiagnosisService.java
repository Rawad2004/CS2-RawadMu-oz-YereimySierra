package app.application.services;

import app.application.port.in.UpdateDiagnosisCommand;
import app.application.usecases.DoctorUseCases.UpdateDiagnosisUseCase;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class UpdateDiagnosisService implements UpdateDiagnosisUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public UpdateDiagnosisService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public ClinicalHistoryEntry updateDiagnosis(UpdateDiagnosisCommand command) {

        ClinicalHistoryEntry entry = clinicalHistoryRepository.findByPatientNationalId(command.patientNationalId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró historia clínica para el paciente: " + command.patientNationalId()));

        if (!entry.hasVisitOnDate(command.visitDate())) {
            throw new IllegalArgumentException(
                    "No existe visita para el paciente " + command.patientNationalId() +
                            " en la fecha: " + command.visitDate());
        }

        String updateNotes = null;
        if (command.diagnosticResults() != null && !command.diagnosticResults().trim().isEmpty()) {
            updateNotes = "Resultados diagnósticos: " + command.diagnosticResults();
        }

        return clinicalHistoryRepository.updateDiagnosis(
                command.patientNationalId(),
                command.visitDate(),
                command.newDiagnosis(),
                updateNotes
        );
    }
}
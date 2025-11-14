// File: src/main/java/app/application/services/UpdateDiagnosisService.java
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
        // Buscar la historia clínica del paciente
        ClinicalHistoryEntry entry = clinicalHistoryRepository.findByPatientNationalId(command.patientNationalId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró historia clínica para el paciente: " + command.patientNationalId()));

        // Verificar que existe la visita
        if (!entry.hasVisitOnDate(command.visitDate())) {
            throw new IllegalArgumentException(
                    "No existe visita para el paciente " + command.patientNationalId() +
                            " en la fecha: " + command.visitDate());
        }

        // Preparar las notas de actualización
        String updateNotes = null;
        if (command.diagnosticResults() != null && !command.diagnosticResults().trim().isEmpty()) {
            updateNotes = "Resultados diagnósticos: " + command.diagnosticResults();
        }

        // Actualizar el diagnóstico
        return clinicalHistoryRepository.updateDiagnosis(
                command.patientNationalId(),
                command.visitDate(),
                command.newDiagnosis(),
                updateNotes
        );
    }
}
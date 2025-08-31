package app.application.usecase;

import app.application.port.in.UpdateClinicalHistoryUseCase;
import app.application.port.in.UpdateDiagnosisCommand;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.vo.NationalId;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateClinicalHistoryService implements UpdateClinicalHistoryUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public UpdateClinicalHistoryService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public ClinicalHistoryEntry updateDiagnosis(UpdateDiagnosisCommand command) {
        ClinicalHistoryEntry entry = clinicalHistoryRepository
                .findByPatientIdAndVisitDate(
                        new NationalId(command.patientNationalId()),
                        command.visitDate()
                )
                .orElseThrow(() -> new IllegalArgumentException("Clinical history entry not found for patient: " + command.patientNationalId() + " on date: " + command.visitDate()));

        entry.updateDiagnosis(command.newDiagnosis());
        return clinicalHistoryRepository.save(entry);
    }
}

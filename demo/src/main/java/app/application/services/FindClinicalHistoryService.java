package app.application.services;

import app.application.usecases.DoctorUseCases;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindClinicalHistoryService implements DoctorUseCases.FindClinicalHistoryUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public FindClinicalHistoryService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public Optional<ClinicalHistoryEntry> findByPatientNationalId(String patientNationalId) {
        if (patientNationalId == null || patientNationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        return clinicalHistoryRepository.findByPatientNationalId(patientNationalId);
    }
}

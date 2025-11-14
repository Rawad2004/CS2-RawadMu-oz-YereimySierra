// File: src/main/java/app/application/services/FindPatientByNationalIdService.java
package app.application.services;

import app.application.usecases.AdministrativeUseCases.FindPatientByNationalIdUseCase;
import app.domain.model.Patient;
import app.domain.model.vo.NationalId;
import app.domain.repository.PatientRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindPatientByNationalIdService implements FindPatientByNationalIdUseCase {

    private final PatientRepositoryPort patientRepository;

    public FindPatientByNationalIdService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Optional<Patient> findByNationalId(String nationalId) {
        if (nationalId == null || nationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía");
        }

        NationalId patientNationalId = new NationalId(nationalId);
        return patientRepository.findByNationalId(patientNationalId);
    }
}
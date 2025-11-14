// File: src/main/java/app/application/services/FindPatientService.java
package app.application.services;

import app.application.usecases.NurseUseCases.FindPatientUseCase;
import app.domain.model.Patient;
import app.domain.model.vo.NationalId;
import app.domain.repository.PatientRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindPatientService implements FindPatientUseCase {

    private final PatientRepositoryPort patientRepository;

    public FindPatientService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Optional<Patient> findPatientByNationalId(String nationalId) {
        return patientRepository.findByNationalId(new NationalId(nationalId));
    }
}
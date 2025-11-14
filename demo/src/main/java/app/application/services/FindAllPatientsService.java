// File: src/main/java/app/application/services/FindAllPatientsService.java
package app.application.services;

import app.application.usecases.AdministrativeUseCases.FindAllPatientsUseCase;
import app.domain.model.Patient;
import app.domain.repository.PatientRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllPatientsService implements FindAllPatientsUseCase {

    private final PatientRepositoryPort patientRepository;

    public FindAllPatientsService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public List<Patient> findAllWithActiveInsurance() {
        return patientRepository.findAll().stream()
                .filter(patient -> patient.getInsurancePolicy() != null && patient.hasActiveInsurance())
                .toList();
    }
}
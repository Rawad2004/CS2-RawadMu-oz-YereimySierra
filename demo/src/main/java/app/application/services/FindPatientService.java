package app.application.services;

import app.application.usecases.AdministrativeUseCases;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.Patient;
import app.domain.model.vo.NationalId;
import app.domain.repository.PatientRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FindPatientService implements AdministrativeUseCases.FindPatientUseCase {

    private final PatientRepositoryPort patientRepository;

    // Constructor manual para asegurar que no haya errores
    public FindPatientService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient findByNationalId(String nationalId) {
        return patientRepository.findByNationalId(new NationalId(nationalId))
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con Cédula: " + nationalId));
    }

    @Override
    public Patient findById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con ID: " + id));
    }

    @Override
    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }
}
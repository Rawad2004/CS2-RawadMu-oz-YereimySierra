package app.application.usecase;

import app.application.port.in.FindPatientUseCase;
import app.domain.model.Patient;
import app.domain.model.vo.NationalId;
import app.domain.repository.PatientRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FindPatientService implements FindPatientUseCase {

    private final PatientRepositoryPort patientRepository;

    public FindPatientService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Optional<Patient> findByNationalId(String nationalId) {
        return patientRepository.findByNationalId(new NationalId(nationalId));
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    @Override
    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }
}

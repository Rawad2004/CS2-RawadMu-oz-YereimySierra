package app.infrastructure.persistence;

import app.domain.model.Patient;
import app.domain.model.vo.NationalId;
import app.domain.repository.PatientRepositoryPort;
import app.infrastructure.persistence.jpa.PatientJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PatientPersistenceAdapter implements PatientRepositoryPort {

    private final PatientJpaRepository patientJpaRepository;

    public PatientPersistenceAdapter(PatientJpaRepository patientJpaRepository) {
        this.patientJpaRepository = patientJpaRepository;
    }

    @Override
    public Patient save(Patient patient) {
        return patientJpaRepository.save(patient);
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return patientJpaRepository.findById(id);
    }

    @Override
    public Optional<Patient> findByNationalId(NationalId nationalId) {
        return patientJpaRepository.findByNationalId(nationalId);
    }

    @Override
    public List<Patient> findAll() {
        return patientJpaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        patientJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByNationalId(NationalId nationalId) {
        return patientJpaRepository.existsByNationalId(nationalId);
    }
}
package app.domain.repository;

import  app.domain.model.Patient;
import app.domain.model.vo.NationalId;
import java.util.List;
import java.util.Optional;
public interface PatientRepositoryPort {
    Patient save(Patient patient);
    Optional<Patient> findById(Long id);
    Optional<Patient> findByNationalId(NationalId nationalId);
    List<Patient> findAll();
    void deleteById(Long id);
    boolean existsByNationalId(NationalId nationalId);
}

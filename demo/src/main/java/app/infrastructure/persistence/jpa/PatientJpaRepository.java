package app.infrastructure.persistence.jpa;

import app.domain.model.Patient;
import app.domain.model.vo.Email;
import app.domain.model.vo.NationalId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientJpaRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByNationalId(NationalId nationalId);
    boolean existsByNationalId(NationalId nationalId);
    Optional<Patient> findByEmail(Email email);
    boolean existsByEmail(Email email);
}
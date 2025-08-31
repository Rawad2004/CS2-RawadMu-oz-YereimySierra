// File: src/main/java/app/application/port/in/FindPatientUseCase.java
package app.application.port.in;

import app.domain.model.Patient;
import java.util.List;
import java.util.Optional;

public interface FindPatientUseCase {
    Optional<Patient> findByNationalId(String nationalId);
    Optional<Patient> findById(Long id);
    List<Patient> findAllPatients();
}
// File: src/main/java/app/application/usecases/AdministrativeUseCases.java
package app.application.usecases;

import app.application.dto.CreatePatientCommand;
import app.application.dto.UpdatePatientCommand;
import app.domain.model.Patient;
import java.util.List;
import java.util.Optional;

public interface AdministrativeUseCases {

    // Interface para crear paciente
    interface CreatePatientUseCase {
        Patient createPatient(CreatePatientCommand command);
    }

    // Interface para actualizar paciente
    interface UpdatePatientUseCase {
        Patient updatePatient(UpdatePatientCommand command);
    }

    // Interface para buscar pacientes
    interface FindPatientUseCase {
        Optional<Patient> findByNationalId(String nationalId);
        Optional<Patient> findById(Long id);
        List<Patient> findAllPatients();
    }
}
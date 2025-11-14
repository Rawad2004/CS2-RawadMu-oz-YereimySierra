package app.application.usecases;

import app.application.port.in.CreatePatientCommand;
import app.application.port.in.UpdatePatientCommand;
import app.domain.model.Patient;
import java.util.List;
import java.util.Optional;

public interface AdministrativeUseCases {

    interface CreatePatientUseCase {
        Patient createPatient(CreatePatientCommand command);
    }

    interface UpdatePatientUseCase {
        Patient updatePatient(UpdatePatientCommand command);
    }

    interface FindAllPatientsUseCase {
        List<Patient> findAll();
        List<Patient> findAllWithActiveInsurance();
    }

    interface FindPatientUseCase {
        Patient findByNationalId(String nationalId);
        Patient findById(Long id);
        List<Patient> findAllPatients();
    }

    interface FindPatientByNationalIdUseCase {
        Optional<Patient> findByNationalId(String nationalId);
    }

    interface DeletePatientUseCase {
        void deletePatient(Long patientId);
        void deletePatientByNationalId(String nationalId);
    }
}
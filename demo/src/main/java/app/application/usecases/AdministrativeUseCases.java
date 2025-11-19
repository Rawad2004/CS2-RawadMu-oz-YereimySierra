package app.application.usecases;

import app.application.port.in.CreatePatientCommand;
import app.application.port.in.UpdatePatientCommand;
import app.domain.model.Patient;

import java.util.List;

public interface AdministrativeUseCases {

    interface CreatePatientUseCase {
        Patient createPatient(CreatePatientCommand command);
    }

    interface UpdatePatientUseCase {
        Patient updatePatient(UpdatePatientCommand command);
    }

    interface FindPatientUseCase {
        Patient findByNationalId(String nationalId);
        Patient findById(Long id);
        List<Patient> findAllPatients();
    }

    interface DeletePatientUseCase {
        void deletePatient(Long patientId);
        void deletePatientByNationalId(String nationalId);
    }
}


package app.application.services;

import app.application.port.in.UpdatePatientCommand;
import app.application.usecases.AdministrativeUseCases;
import app.domain.model.Patient;
import app.domain.model.enums.Gender;
import app.domain.model.vo.*;
import app.domain.repository.PatientRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdatePatientService implements AdministrativeUseCases.UpdatePatientUseCase {

    private final PatientRepositoryPort patientRepository;

    public UpdatePatientService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient updatePatient(UpdatePatientCommand command) {


        Patient existingPatient = patientRepository.findByNationalId(new NationalId(command.nationalId()))
                .orElseThrow(() ->
                        new IllegalArgumentException("Paciente no encontrado con cédula: " + command.nationalId())
                );


        Email email = null;
        if (command.email() != null && !command.email().trim().isEmpty()) {
            email = new Email(command.email());
        }


        EmergencyContact emergencyContact = new EmergencyContact(
                command.emergencyContact().fullName(),
                command.emergencyContact().relationship(),
                new PhoneNumber(command.emergencyContact().phoneNumber())
        );


        InsurancePolicy insurancePolicy = new InsurancePolicy(
                command.insurancePolicy().companyName(),
                command.insurancePolicy().policyNumber(),
                command.insurancePolicy().active(),
                command.insurancePolicy().expiryDate()
        );


        existingPatient.updateBasicInfo(
                command.fullName(),
                command.birthDate(),
                command.gender()
        );


        existingPatient.updateContactInfo(
                new Address(command.address()),
                new PhoneNumber(command.phoneNumber()),
                email
        );


        existingPatient.updateEmergencyContact(emergencyContact);


        existingPatient.updateInsurancePolicy(insurancePolicy);


        return patientRepository.save(existingPatient);
    }
}

package app.application.services;

import app.application.port.in.CreatePatientCommand;
import app.application.usecases.AdministrativeUseCases.CreatePatientUseCase;
import app.domain.model.Patient;
import app.domain.model.vo.*;
import app.domain.repository.PatientRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreatePatientService implements CreatePatientUseCase {

    private final PatientRepositoryPort patientRepository;

    public CreatePatientService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient createPatient(CreatePatientCommand command) {

        NationalId nationalId = new NationalId(command.nationalId());
        if (patientRepository.existsByNationalId(nationalId)) {
            throw new IllegalStateException("Ya existe un paciente con la cédula: " + command.nationalId());
        }

        Email email = null;
        if (command.email() != null && !command.email().trim().isEmpty()) {
            email = new Email(command.email());


            if (patientRepository.findByEmail(new Email(command.email())).isPresent()) {
                throw new IllegalStateException("Ya existe un paciente con el email: " + command.email());
            }
        }


        if (command.insurancePolicy().active()) {
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


        Patient newPatient = new Patient(
                nationalId,
                command.fullName(),
                new DateOfBirth(command.birthDate()),
                command.gender(),
                new Address(command.address()),
                new PhoneNumber(command.phoneNumber()),
                email,
                emergencyContact,
                insurancePolicy
        );

        return patientRepository.save(newPatient);
    }
}
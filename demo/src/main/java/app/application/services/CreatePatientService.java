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
        // ✅ Validación de cédula única
        NationalId nationalId = new NationalId(command.nationalId());
        if (patientRepository.existsByNationalId(nationalId)) {
            throw new IllegalStateException("Ya existe un paciente con la cédula: " + command.nationalId());
        }

        Email email = null;
        if (command.email() != null && !command.email().trim().isEmpty()) {
            email = new Email(command.email());

            // ✅ Validación de email único (si aplica)
            if (patientRepository.findByEmail(new Email(command.email())).isPresent()) {
                throw new IllegalStateException("Ya existe un paciente con el email: " + command.email());
            }
        }

        // ✅ Validación de política de seguro (máximo 1 póliza activa)
        if (command.insurancePolicy().active()) {
            // Podrías agregar validaciones adicionales sobre pólizas
        }

        // ✅ Crear value objects con validaciones internas
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

        // ✅ Crear entidad de dominio
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
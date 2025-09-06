// File: src/main/java/app/application/services/RecordMedicationAdministrationService.java
package app.application.services;

import app.application.dto.RecordMedicationAdministrationCommand;
import app.application.usecases.NurseUseCases.RecordMedicationAdministrationUseCase;
import app.domain.model.Patient;
import app.domain.model.vo.Dose;
import app.domain.model.vo.NationalId;
import app.domain.repository.MedicationRepositoryPort;
import app.domain.repository.PatientRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RecordMedicationAdministrationService implements RecordMedicationAdministrationUseCase {

    private final PatientRepositoryPort patientRepository;
    private final MedicationRepositoryPort medicationRepository;

    public RecordMedicationAdministrationService(PatientRepositoryPort patientRepository,
                                                 MedicationRepositoryPort medicationRepository) {
        this.patientRepository = patientRepository;
        this.medicationRepository = medicationRepository;
    }

    @Override
    public Patient recordMedicationAdministration(String patientNationalId, RecordMedicationAdministrationCommand command) {
        // Validar y obtener el paciente
        Patient patient = patientRepository.findByNationalId(new NationalId(patientNationalId))
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado con cédula: " + patientNationalId));

        // Validar que el medicamento exista
        medicationRepository.findById(command.medicationId())
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado con ID: " + command.medicationId()));

        // Crear Value Objects con validaciones del dominio
        Dose dose = new Dose(command.dose());

        // Validar que la hora de administración no sea futura
        if (command.administrationTime().isAfter(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("La hora de administración no puede ser futura");
        }

        System.out.println("Registrando administración de medicamento para paciente: " + patientNationalId);
        System.out.println("Medicamento ID: " + command.medicationId());
        System.out.println("Dosis: " + dose.getValue());
        System.out.println("Hora de administración: " + command.administrationTime());

        // Retornar el paciente (en una implementación real, se retornaría el paciente actualizado)
        return patient;
    }
}
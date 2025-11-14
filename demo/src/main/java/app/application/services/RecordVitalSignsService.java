// File: src/main/java/app/application/services/RecordVitalSignsService.java
package app.application.services;

import app.application.port.in.RecordVitalSignsCommand;
import app.application.usecases.NurseUseCases.RecordVitalSignsUseCase;
import app.domain.model.Patient;
import app.domain.model.VitalSignsEntry;
import app.domain.model.vo.*;
import app.domain.repository.PatientRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class RecordVitalSignsService implements RecordVitalSignsUseCase {

    private final PatientRepositoryPort patientRepository;

    public RecordVitalSignsService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient recordVitalSigns(String patientNationalId, RecordVitalSignsCommand command) {
        Patient patient = patientRepository.findByNationalId(new NationalId(patientNationalId))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Paciente no encontrado con cédula: " + patientNationalId));

        // Crear Value Objects
        BloodPressure bloodPressure = new BloodPressure(command.bloodPressure());
        Temperature temperature = new Temperature(command.temperature());
        Pulse pulse = new Pulse(command.pulse());
        OxygenLevel oxygenLevel = new OxygenLevel(command.oxygenLevel());

        // Crear entrada con Value Objects directamente
        VitalSignsEntry entry = new VitalSignsEntry(
                bloodPressure,    // BloodPressure VO
                temperature,      // Temperature VO
                pulse,            // Pulse VO
                oxygenLevel,      // OxygenLevel VO
                LocalDate.now()   // LocalDate
        );

        patient.addVitalSigns(entry);
        Patient savedPatient = patientRepository.save(patient);

        System.out.println("✅ Signos vitales registrados - Paciente: " + patientNationalId +
                ", Presión: " + bloodPressure.getValue() +
                ", Temp: " + temperature.getValue() +
                ", Pulso: " + pulse.getValue() +
                ", Oxígeno: " + oxygenLevel.getValue());

        return savedPatient;
    }
}
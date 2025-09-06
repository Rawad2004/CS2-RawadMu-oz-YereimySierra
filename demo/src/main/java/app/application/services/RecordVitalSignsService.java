// File: src/main/java/app/application/services/RecordVitalSignsService.java
package app.application.services;

import app.application.dto.RecordVitalSignsCommand;
import app.application.usecases.NurseUseCases.RecordVitalSignsUseCase;
import app.domain.model.Patient;
import app.domain.model.vo.BloodPressure;
import app.domain.model.vo.NationalId;
import app.domain.model.vo.OxygenLevel;
import app.domain.model.vo.Pulse;
import app.domain.model.vo.Temperature;
import app.domain.repository.PatientRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RecordVitalSignsService implements RecordVitalSignsUseCase {

    private final PatientRepositoryPort patientRepository;

    public RecordVitalSignsService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient recordVitalSigns(String patientNationalId, RecordVitalSignsCommand command) {
        // Validar y obtener el paciente
        Patient patient = patientRepository.findByNationalId(new NationalId(patientNationalId))
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado con cédula: " + patientNationalId));

        // Crear Value Objects con validaciones del dominio
        BloodPressure bloodPressure = new BloodPressure(
                command.systolicBloodPressure(),
                command.diastolicBloodPressure()
        );

        Temperature temperature = new Temperature(command.temperature());
        Pulse pulse = new Pulse(command.pulse());
        OxygenLevel oxygenLevel = new OxygenLevel(command.oxygenLevel());

        System.out.println("Registrando signos vitales para paciente: " + patientNationalId);
        System.out.println("Presión arterial: " + bloodPressure.toString());
        System.out.println("Temperatura: " + temperature.toString());
        System.out.println("Pulso: " + pulse.toString());
        System.out.println("Oxígeno: " + oxygenLevel.toString());

        // Retornar el paciente (en una implementación real, se retornaría el paciente actualizado)
        return patient;
    }
}
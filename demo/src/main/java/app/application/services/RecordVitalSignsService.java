// File: src/main/java/app/application/services/RecordVitalSignsService.java
package app.application.services;

import app.application.dto.RecordVitalSignsCommand;
import app.application.usecases.NurseUseCases.RecordVitalSignsUseCase;
import app.domain.model.Patient;
import app.domain.model.VitalSignsEntry;
import app.domain.model.vo.NationalId;
import app.domain.model.vo.OxygenLevel;
import app.domain.model.vo.Pulse;
import app.domain.model.vo.Temperature;
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


        new Temperature(command.temperature());
        new Pulse(command.pulse());
        new OxygenLevel(command.oxygenLevel());


        VitalSignsEntry entry = new VitalSignsEntry(
                command.bloodPressure(),
                command.temperature(),
                command.pulse(),
                command.oxygenLevel(),
                LocalDate.now()
        );


        patient.addVitalSigns(entry);
        patientRepository.save(patient);

        return patient;
    }
}

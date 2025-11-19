package app.application.services;

import app.application.port.in.RecordVitalSignsCommand;
import app.application.usecases.NurseUseCases;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.Patient;
import app.domain.model.vo.NationalId;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import app.domain.repository.PatientRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class RecordVitalSignsService implements NurseUseCases.RecordVitalSignsUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;
    private final PatientRepositoryPort patientRepository;

    public RecordVitalSignsService(ClinicalHistoryRepositoryPort clinicalHistoryRepository,
                                   PatientRepositoryPort patientRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient recordVitalSigns(String patientNationalId,
                                    RecordVitalSignsCommand command,
                                    LocalDate visitDate) {


        Patient patient = patientRepository.findByNationalId(new NationalId(patientNationalId))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Paciente no encontrado con cédula: " + patientNationalId
                ));

        ClinicalHistoryEntry entry = clinicalHistoryRepository
                .findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró historia clínica para el paciente: " + patientNationalId
                ));


        if (!entry.hasVisitOnDate(visitDate)) {
            throw new IllegalArgumentException(
                    "No existe visita para el paciente " + patientNationalId +
                            " en la fecha " + visitDate
            );
        }


        String bloodPressureStr = String.valueOf(command.bloodPressure());

        entry.registerVitalSigns(
                visitDate,
                bloodPressureStr,
                command.temperature(),
                command.pulse(),
                null,
                null
        );


        clinicalHistoryRepository.save(entry);

        return patient;
    }
}

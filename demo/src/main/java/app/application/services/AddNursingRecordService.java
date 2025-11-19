package app.application.services;

import app.application.port.in.AddNursingRecordCommand;
import app.application.usecases.NurseUseCases.AddNursingRecordUseCase;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class AddNursingRecordService implements AddNursingRecordUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public AddNursingRecordService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public void addNursingRecord(String patientNationalId,
                                 LocalDate visitDate,
                                 AddNursingRecordCommand command) {

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

        entry.addNursingRecord(
                visitDate,
                command.orderNumber(),
                command.itemNumber(),
                command.description()
        );

        clinicalHistoryRepository.save(entry);
    }
}

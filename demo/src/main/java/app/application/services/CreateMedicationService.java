package app.application.services;

import app.application.port.in.CreateMedicationCommand;
import app.application.usecases.SupportUseCases.CreateMedicationUseCase;
import app.domain.model.Medication;
import app.domain.model.vo.Money;
import app.domain.repository.MedicationRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateMedicationService implements CreateMedicationUseCase {

    private final MedicationRepositoryPort medicationRepository;

    public CreateMedicationService(MedicationRepositoryPort medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Override
    public Medication createMedication(CreateMedicationCommand command) {

        medicationRepository.findByName(command.name()).ifPresent(existing -> {
            throw new IllegalStateException("Ya existe un medicamento con el nombre: " + command.name());
        });

        Medication med = new Medication(
                command.name(),
                new Money(command.cost())
        );

        return medicationRepository.save(med);
    }
}

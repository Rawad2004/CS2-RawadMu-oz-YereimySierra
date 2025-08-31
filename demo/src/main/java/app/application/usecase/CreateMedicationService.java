package app.application.usecase;

import app.application.port.in.CreateMedicationCommand;
import app.application.port.in.CreateMedicationUseCase;
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

        if (medicationRepository.findByName(command.name()).isPresent()) {
            throw new IllegalStateException("Ya existe un medicamento con el nombre: " + command.name());
        }


        Medication newMedication = new Medication(
                command.name(),
                new Money(command.cost())
        );


        return medicationRepository.save(newMedication);
    }
}
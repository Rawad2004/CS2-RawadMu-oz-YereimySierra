package app.application.services;

import app.application.dto.CreateMedicationCommand;
import app.application.usecases.SupportUseCases.CreateMedicationUseCase;
import app.domain.model.Medication;
import app.domain.model.vo.Money;
import app.infrastructure.persistence.jpa.MedicationJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateMedicationService implements CreateMedicationUseCase {

    private final MedicationJpaRepository medicationRepository;

    public CreateMedicationService(MedicationJpaRepository medicationRepository) {
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
package app.application.services;

import app.application.port.in.UpdateMedicationCommand;
import app.application.usecases.SupportUseCases.UpdateMedicationUseCase;
import app.domain.model.Medication;
import app.domain.model.vo.Money;
import app.domain.repository.MedicationRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateMedicationService implements UpdateMedicationUseCase {

    private final MedicationRepositoryPort medicationRepository;

    public UpdateMedicationService(MedicationRepositoryPort medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Override
    public Medication updateMedication(UpdateMedicationCommand command) {
        if (command.medicationId() == null || command.medicationId() <= 0) {
            throw new IllegalArgumentException("ID de medicamento inválido");
        }


        Medication existingMedication = medicationRepository.findById(command.medicationId())
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado con ID: " + command.medicationId()));


        if (command.name() != null && !command.name().equals(existingMedication.getName())) {
            medicationRepository.findByName(command.name())
                    .ifPresent(medication -> {
                        throw new IllegalStateException("Ya existe un medicamento con el nombre: " + command.name());
                    });
        }


        if (command.name() != null) {

        }

        if (command.cost() != null) {
            Money newCost = new Money(command.cost());

        }


        System.out.println("✅ Medicamento actualizado - ID: " + command.medicationId() +
                ", Nombre: " + command.name() +
                ", Costo: " + command.cost());

        return medicationRepository.save(existingMedication);
    }
}
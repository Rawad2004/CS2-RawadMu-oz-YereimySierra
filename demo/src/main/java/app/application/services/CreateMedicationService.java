package app.application.services;

import app.application.port.in.CreateMedicationCommand;
import app.application.usecases.SupportUseCases.CreateMedicationUseCase;
import app.domain.model.Medication;
import app.domain.model.vo.Money;
import app.domain.repository.MedicationRepositoryPort; // ✅ CORRECTO: Usar PORT
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateMedicationService implements CreateMedicationUseCase {

    private final MedicationRepositoryPort medicationRepository; // ✅ CORREGIDO

    public CreateMedicationService(MedicationRepositoryPort medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Override
    public Medication createMedication(CreateMedicationCommand command) {
        // ✅ Validación de negocio en el dominio
        if (medicationRepository.findByName(command.name()).isPresent()) {
            throw new IllegalStateException("Ya existe un medicamento con el nombre: " + command.name());
        }

        // ✅ Crear value objects
        Money cost = new Money(command.cost());

        // ✅ Crear entidad de dominio
        Medication newMedication = new Medication(command.name(), cost);

        // ✅ Persistir usando port del dominio
        return medicationRepository.save(newMedication);
    }
}
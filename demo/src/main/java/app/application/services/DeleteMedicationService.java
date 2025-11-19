package app.application.services;

import app.application.usecases.SupportUseCases.DeleteMedicationUseCase;
import app.domain.repository.MedicationRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteMedicationService implements DeleteMedicationUseCase {

    private final MedicationRepositoryPort medicationRepository;

    public DeleteMedicationService(MedicationRepositoryPort medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Override
    public void deleteMedication(Long id) {
        if (!medicationRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe medicamento con ID: " + id);
        }
        medicationRepository.deleteById(id);
    }

    @Override
    public void deleteMedicationByName(String name) {
        if (!medicationRepository.existsByName(name)) {
            throw new IllegalArgumentException("No existe medicamento con nombre: " + name);
        }
        medicationRepository.deleteByName(name);
    }
}

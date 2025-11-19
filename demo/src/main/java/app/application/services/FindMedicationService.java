package app.application.services;

import app.application.usecases.SupportUseCases.FindMedicationUseCase;
import app.domain.model.Medication;
import app.domain.repository.MedicationRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FindMedicationService implements FindMedicationUseCase {

    private final MedicationRepositoryPort medicationRepository;

    public FindMedicationService(MedicationRepositoryPort medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Override
    public Optional<Medication> findById(Long id) {
        return medicationRepository.findById(id);
    }

    @Override
    public Optional<Medication> findByName(String name) {
        return medicationRepository.findByName(name);
    }

    @Override
    public List<Medication> findAll() {
        return medicationRepository.findAll();
    }

    @Override
    public List<Medication> findAllActive() {
        // Si no tienes un campo "active", por ahora devolvemos todos
        return medicationRepository.findAll();
    }
}

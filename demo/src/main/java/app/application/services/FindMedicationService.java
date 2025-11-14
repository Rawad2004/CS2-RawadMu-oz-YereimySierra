// File: src/main/java/app/application/services/FindMedicationService.java
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
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de medicamento inválido");
        }
        return medicationRepository.findById(id);
    }

    @Override
    public Optional<Medication> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de medicamento no puede estar vacío");
        }
        return medicationRepository.findByName(name);
    }

    @Override
    public List<Medication> findAll() {
        return medicationRepository.findAll();
    }

    @Override
    public List<Medication> findAllActive() {
        // Por ahora todos los medicamentos se consideran activos
        // En una implementación futura podríamos agregar campo "active"
        return medicationRepository.findAll();
    }
}
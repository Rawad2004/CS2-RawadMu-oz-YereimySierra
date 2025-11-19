package app.application.services;

import app.application.usecases.SupportUseCases.FindSpecialistUseCase;
import app.domain.model.Specialist;
import app.domain.repository.SpecialistRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FindSpecialistService implements FindSpecialistUseCase {

    private final SpecialistRepositoryPort specialistRepository;

    public FindSpecialistService(SpecialistRepositoryPort specialistRepository) {
        this.specialistRepository = specialistRepository;
    }

    @Override
    public Optional<Specialist> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de especialista inválido");
        }
        return specialistRepository.findById(id);
    }

    @Override
    public Optional<Specialist> findBySpecialtyName(String specialtyName) {
        if (specialtyName == null || specialtyName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la especialidad no puede estar vacío");
        }
        return specialistRepository.findBySpecialtyName(specialtyName);
    }

    @Override
    public List<Specialist> findAll() {
        return specialistRepository.findAll();
    }
}

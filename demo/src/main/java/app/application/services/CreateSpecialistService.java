package app.application.services;

import app.application.usecases.SupportUseCases.CreateSpecialistUseCase;
import app.application.dto.CreateSpecialistCommand;
import app.domain.model.Specialist;
import app.domain.repository.SpecialistRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateSpecialistService implements CreateSpecialistUseCase {

    private final SpecialistRepositoryPort specialistRepository;

    public CreateSpecialistService(SpecialistRepositoryPort specialistRepository) {
        this.specialistRepository = specialistRepository;
    }

    @Override
    public Specialist createSpecialist(CreateSpecialistCommand command) {
        if (specialistRepository.findBySpecialtyName(command.specialtyName()).isPresent()) {
            throw new IllegalStateException("Specialty already exists: " + command.specialtyName());
        }

        Specialist newSpecialist = new Specialist(command.specialtyName());
        return specialistRepository.save(newSpecialist);
    }
}
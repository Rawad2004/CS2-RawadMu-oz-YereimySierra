package app.application.services;

import app.application.port.in.UpdateSpecialistCommand;
import app.application.usecases.SupportUseCases.UpdateSpecialistUseCase;
import app.domain.model.Specialist;
import app.domain.repository.SpecialistRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateSpecialistService implements UpdateSpecialistUseCase {

    private final SpecialistRepositoryPort specialistRepository;

    public UpdateSpecialistService(SpecialistRepositoryPort specialistRepository) {
        this.specialistRepository = specialistRepository;
    }

    @Override
    public Specialist updateSpecialist(UpdateSpecialistCommand command) {
        if (command.specialistId() == null || command.specialistId() <= 0) {
            throw new IllegalArgumentException("ID de especialista inválido");
        }

        Specialist existing = specialistRepository.findById(command.specialistId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Especialista no encontrado con ID: " + command.specialistId()
                ));


        if (command.specialtyName() != null &&
                !command.specialtyName().equals(existing.getSpecialtyName())) {

            specialistRepository.findBySpecialtyName(command.specialtyName())
                    .ifPresent(s -> {
                        throw new IllegalStateException(
                                "Ya existe un especialista con la especialidad: " + command.specialtyName()
                        );
                    });


        }

        System.out.println("✅ Especialista actualizado - ID: " + command.specialistId() +
                ", Especialidad: " + command.specialtyName());

        return specialistRepository.save(existing);
    }
}

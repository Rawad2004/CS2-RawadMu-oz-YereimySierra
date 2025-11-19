package app.application.services;

import app.application.usecases.SupportUseCases.DeleteSpecialistUseCase;
import app.domain.repository.SpecialistRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteSpecialistService implements DeleteSpecialistUseCase {

    private final SpecialistRepositoryPort specialistRepository;

    public DeleteSpecialistService(SpecialistRepositoryPort specialistRepository) {
        this.specialistRepository = specialistRepository;
    }

    @Override
    public void deleteSpecialist(Long specialistId) {
        if (specialistId == null || specialistId <= 0) {
            throw new IllegalArgumentException("ID de especialista inválido");
        }

        specialistRepository.findById(specialistId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Especialista no encontrado con ID: " + specialistId
                ));

        specialistRepository.deleteById(specialistId);
    }

    @Override
    public void deleteSpecialistBySpecialtyName(String specialtyName) {
        if (specialtyName == null || specialtyName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la especialidad no puede estar vacío");
        }

        specialistRepository.findBySpecialtyName(specialtyName)
                .ifPresent(specialist -> deleteSpecialist(specialist.getId()));
    }
}

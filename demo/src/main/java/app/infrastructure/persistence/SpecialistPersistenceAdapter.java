package app.infrastructure.persistence;

import app.domain.model.Specialist;
import app.domain.repository.SpecialistRepositoryPort;
import app.infrastructure.persistence.jpa.SpecialistJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SpecialistPersistenceAdapter implements SpecialistRepositoryPort {

    private final SpecialistJpaRepository specialistJpaRepository;

    public SpecialistPersistenceAdapter(SpecialistJpaRepository specialistJpaRepository) {
        this.specialistJpaRepository = specialistJpaRepository;
    }

    @Override
    public Specialist save(Specialist specialist) {
        return specialistJpaRepository.save(specialist);
    }

    @Override
    public Optional<Specialist> findById(Long id) {
        return specialistJpaRepository.findById(id);
    }

    @Override
    public Optional<Specialist> findBySpecialtyName(String name) {
        return specialistJpaRepository.findBySpecialtyName(name);
    }

    @Override
    public List<Specialist> findAll() {
        return specialistJpaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        specialistJpaRepository.deleteById(id);
    }
}

package app.domain.repository;

import app.domain.model.Specialist;
import java.util.List;
import java.util.Optional;

public interface SpecialistRepositoryPort {
    Specialist save(Specialist specialist);
    Optional<Specialist> findById(Long id);
    Optional<Specialist> findBySpecialtyName(String name);
    List<Specialist> findAll();
    void deleteById(Long id);
}
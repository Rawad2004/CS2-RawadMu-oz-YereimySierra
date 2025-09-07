package app.infrastructure.persistence.jpa;

import app.domain.model.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpecialistJpaRepository extends JpaRepository<Specialist, Long> {
    Optional<Specialist> findBySpecialtyName(String name);
}
package app.infrastructure.persistence.jpa;

import app.domain.model.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProcedureJpaRepository extends JpaRepository<Procedure, Long> {
    Optional<Procedure> findByName(String name);
}
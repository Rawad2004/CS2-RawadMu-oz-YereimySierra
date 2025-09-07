package app.infrastructure.persistence.jpa;

import app.domain.model.DiagnosticAid;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DiagnosticAidJpaRepository extends JpaRepository<DiagnosticAid, Long> {
    Optional<DiagnosticAid> findByName(String name);
}
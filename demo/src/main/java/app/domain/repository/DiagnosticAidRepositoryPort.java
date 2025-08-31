package app.domain.repository;

import app.domain.model.DiagnosticAid;
import java.util.List;
import java.util.Optional;

public interface DiagnosticAidRepositoryPort {
    DiagnosticAid save(DiagnosticAid diagnosticAid);
    Optional<DiagnosticAid> findById(Long id);
    Optional<DiagnosticAid> findByName(String name);
    List<DiagnosticAid> findAll();
    void deleteById(Long id);
}
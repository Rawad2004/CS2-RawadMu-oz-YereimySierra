package app.infrastructure.persistence;

import app.domain.model.DiagnosticAid;
import app.domain.repository.DiagnosticAidRepositoryPort;
import app.infrastructure.persistence.jpa.DiagnosticAidJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DiagnosticAidPersistenceAdapter implements DiagnosticAidRepositoryPort {

    private final DiagnosticAidJpaRepository diagnosticAidJpaRepository;

    public DiagnosticAidPersistenceAdapter(DiagnosticAidJpaRepository diagnosticAidJpaRepository) {
        this.diagnosticAidJpaRepository = diagnosticAidJpaRepository;
    }

    @Override
    public DiagnosticAid save(DiagnosticAid diagnosticAid) {
        return diagnosticAidJpaRepository.save(diagnosticAid);
    }

    @Override
    public Optional<DiagnosticAid> findById(Long id) {
        return diagnosticAidJpaRepository.findById(id);
    }

    @Override
    public Optional<DiagnosticAid> findByName(String name) {
        return diagnosticAidJpaRepository.findByName(name);
    }

    @Override
    public List<DiagnosticAid> findAll() {
        return diagnosticAidJpaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        diagnosticAidJpaRepository.deleteById(id);
    }
}
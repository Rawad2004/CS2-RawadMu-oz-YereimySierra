// Ubicación: app.infrastructure.persistence/ProcedurePersistenceAdapter.java
package app.infrastructure.persistence;

import app.domain.model.Procedure;
import app.domain.repository.ProcedureRepositoryPort;
import app.infrastructure.persistence.jpa.ProcedureJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProcedurePersistenceAdapter implements ProcedureRepositoryPort {

    private final ProcedureJpaRepository procedureJpaRepository;

    public ProcedurePersistenceAdapter(ProcedureJpaRepository procedureJpaRepository) {
        this.procedureJpaRepository = procedureJpaRepository;
    }

    @Override
    public Procedure save(Procedure procedure) {
        return procedureJpaRepository.save(procedure);
    }

    @Override
    public Optional<Procedure> findById(Long id) {
        return procedureJpaRepository.findById(id);
    }

    @Override
    public Optional<Procedure> findByName(String name) {
        return procedureJpaRepository.findByName(name);
    }

    @Override
    public List<Procedure> findAll() {
        return procedureJpaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        procedureJpaRepository.deleteById(id);
    }
}
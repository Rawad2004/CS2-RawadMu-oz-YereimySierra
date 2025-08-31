package app.domain.repository;

import app.domain.model.Procedure;
import java.util.List;
import java.util.Optional;

public interface ProcedureRepositoryPort {
    Procedure save(Procedure procedure);
    Optional<Procedure> findById(Long id);
    Optional<Procedure> findByName(String name);
    List<Procedure> findAll();
    void deleteById(Long id);
}
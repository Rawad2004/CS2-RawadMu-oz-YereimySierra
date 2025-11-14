// File: src/main/java/app/application/services/FindProcedureService.java
package app.application.services;

import app.application.usecases.SupportUseCases.FindProcedureUseCase;
import app.domain.model.Procedure;
import app.domain.repository.ProcedureRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FindProcedureService implements FindProcedureUseCase {

    private final ProcedureRepositoryPort procedureRepository;

    public FindProcedureService(ProcedureRepositoryPort procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @Override
    public Optional<Procedure> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de procedimiento inválido");
        }
        return procedureRepository.findById(id);
    }

    @Override
    public Optional<Procedure> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de procedimiento no puede estar vacío");
        }
        return procedureRepository.findByName(name);
    }

    @Override
    public List<Procedure> findAll() {
        return procedureRepository.findAll();
    }
}
package app.application.services;

import app.application.usecases.SupportUseCases.FindDiagnosticAidUseCase;
import app.domain.model.DiagnosticAid;
import app.domain.repository.DiagnosticAidRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FindDiagnosticAidService implements FindDiagnosticAidUseCase {

    private final DiagnosticAidRepositoryPort diagnosticAidRepository;

    public FindDiagnosticAidService(DiagnosticAidRepositoryPort diagnosticAidRepository) {
        this.diagnosticAidRepository = diagnosticAidRepository;
    }

    @Override
    public Optional<DiagnosticAid> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de ayuda diagnóstica inválido");
        }
        return diagnosticAidRepository.findById(id);
    }

    @Override
    public Optional<DiagnosticAid> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de ayuda diagnóstica no puede estar vacío");
        }
        return diagnosticAidRepository.findByName(name);
    }

    @Override
    public List<DiagnosticAid> findAll() {
        return diagnosticAidRepository.findAll();
    }
}
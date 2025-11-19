package app.application.services;

import app.application.usecases.SupportUseCases.DeleteDiagnosticAidUseCase;
import app.domain.repository.DiagnosticAidRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteDiagnosticAidService implements DeleteDiagnosticAidUseCase {

    private final DiagnosticAidRepositoryPort diagnosticAidRepository;

    public DeleteDiagnosticAidService(DiagnosticAidRepositoryPort diagnosticAidRepository) {
        this.diagnosticAidRepository = diagnosticAidRepository;
    }

    @Override
    public void deleteDiagnosticAid(Long diagnosticAidId) {
        if (diagnosticAidId == null || diagnosticAidId <= 0) {
            throw new IllegalArgumentException("ID de ayuda diagnóstica inválido");
        }


        diagnosticAidRepository.findById(diagnosticAidId)
                .orElseThrow(() -> new IllegalArgumentException("Ayuda diagnóstica no encontrada con ID: " + diagnosticAidId));


        if (isDiagnosticAidUsedInActiveOrders(diagnosticAidId)) {
            throw new IllegalStateException(
                    "No se puede eliminar la ayuda diagnóstica porque está siendo usado en órdenes médicas activas."
            );
        }

        diagnosticAidRepository.deleteById(diagnosticAidId);
    }

    @Override
    public void deleteDiagnosticAidByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de ayuda diagnóstica no puede estar vacío");
        }

        diagnosticAidRepository.findByName(name)
                .ifPresent(diagnosticAid -> deleteDiagnosticAid(diagnosticAid.getId()));
    }

    private boolean isDiagnosticAidUsedInActiveOrders(Long diagnosticAidId) {

        return false;
    }
}
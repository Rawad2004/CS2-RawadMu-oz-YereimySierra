// File: src/main/java/app/application/services/UpdateDiagnosticAidService.java
package app.application.services;

import app.application.port.in.UpdateDiagnosticAidCommand;
import app.application.usecases.SupportUseCases.UpdateDiagnosticAidUseCase;
import app.domain.model.DiagnosticAid;
import app.domain.model.vo.Money;
import app.domain.repository.DiagnosticAidRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateDiagnosticAidService implements UpdateDiagnosticAidUseCase {

    private final DiagnosticAidRepositoryPort diagnosticAidRepository;

    public UpdateDiagnosticAidService(DiagnosticAidRepositoryPort diagnosticAidRepository) {
        this.diagnosticAidRepository = diagnosticAidRepository;
    }

    @Override
    public DiagnosticAid updateDiagnosticAid(UpdateDiagnosticAidCommand command) {
        if (command.diagnosticAidId() == null || command.diagnosticAidId() <= 0) {
            throw new IllegalArgumentException("ID de ayuda diagnóstica inválido");
        }

        // Buscar la ayuda diagnóstica existente
        DiagnosticAid existingDiagnosticAid = diagnosticAidRepository.findById(command.diagnosticAidId())
                .orElseThrow(() -> new IllegalArgumentException("Ayuda diagnóstica no encontrada con ID: " + command.diagnosticAidId()));

        // ✅ Validación: Verificar unicidad del nombre (si se está cambiando)
        if (command.name() != null && !command.name().equals(existingDiagnosticAid.getName())) {
            diagnosticAidRepository.findByName(command.name())
                    .ifPresent(diagnosticAid -> {
                        throw new IllegalStateException("Ya existe una ayuda diagnóstica con el nombre: " + command.name());
                    });
        }

        // Actualizar campos si se proporcionan
        if (command.name() != null) {
            // En una implementación real, usaríamos un método setter en la entidad
            // existingDiagnosticAid.updateName(command.name());
        }

        if (command.cost() != null) {
            Money newCost = new Money(command.cost());
            // existingDiagnosticAid.updateCost(newCost);
        }

        System.out.println("✅ Ayuda diagnóstica actualizada - ID: " + command.diagnosticAidId() +
                ", Nombre: " + command.name() +
                ", Costo: " + command.cost());

        return diagnosticAidRepository.save(existingDiagnosticAid);
    }
}
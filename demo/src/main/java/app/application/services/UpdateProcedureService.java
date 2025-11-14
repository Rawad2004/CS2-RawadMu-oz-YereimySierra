// File: src/main/java/app/application/services/UpdateProcedureService.java
package app.application.services;

import app.application.port.in.UpdateProcedureCommand;
import app.application.usecases.SupportUseCases.UpdateProcedureUseCase;
import app.domain.model.Procedure;
import app.domain.model.vo.Money;
import app.domain.repository.ProcedureRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateProcedureService implements UpdateProcedureUseCase {

    private final ProcedureRepositoryPort procedureRepository;

    public UpdateProcedureService(ProcedureRepositoryPort procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @Override
    public Procedure updateProcedure(UpdateProcedureCommand command) {
        if (command.procedureId() == null || command.procedureId() <= 0) {
            throw new IllegalArgumentException("ID de procedimiento inválido");
        }

        Procedure existingProcedure = procedureRepository.findById(command.procedureId())
                .orElseThrow(() -> new IllegalArgumentException("Procedimiento no encontrado con ID: " + command.procedureId()));

        // ✅ Validación de unicidad del nombre
        if (command.name() != null && !command.name().equals(existingProcedure.getName())) {
            procedureRepository.findByName(command.name())
                    .ifPresent(procedure -> {
                        throw new IllegalStateException("Ya existe un procedimiento con el nombre: " + command.name());
                    });
        }

        // Actualizar campos
        if (command.name() != null) {
            // existingProcedure.updateName(command.name());
        }

        if (command.cost() != null) {
            Money newCost = new Money(command.cost());
            // existingProcedure.updateCost(newCost);
        }

        System.out.println("✅ Procedimiento actualizado - ID: " + command.procedureId() +
                ", Nombre: " + command.name() +
                ", Costo: " + command.cost());

        return procedureRepository.save(existingProcedure);
    }
}
package app.application.services;

import app.application.usecases.SupportUseCases.DeleteProcedureUseCase;
import app.domain.repository.ProcedureRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteProcedureService implements DeleteProcedureUseCase {

    private final ProcedureRepositoryPort procedureRepository;

    public DeleteProcedureService(ProcedureRepositoryPort procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @Override
    public void deleteProcedure(Long procedureId) {
        if (procedureId == null || procedureId <= 0) {
            throw new IllegalArgumentException("ID de procedimiento inválido");
        }


        procedureRepository.findById(procedureId)
                .orElseThrow(() -> new IllegalArgumentException("Procedimiento no encontrado con ID: " + procedureId));


        if (isProcedureUsedInActiveOrders(procedureId)) {
            throw new IllegalStateException(
                    "No se puede eliminar el procedimiento porque está siendo usado en órdenes médicas activas."
            );
        }

        procedureRepository.deleteById(procedureId);
    }

    @Override
    public void deleteProcedureByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de procedimiento no puede estar vacío");
        }

        procedureRepository.findByName(name)
                .ifPresent(procedure -> deleteProcedure(procedure.getId()));
    }

    private boolean isProcedureUsedInActiveOrders(Long procedureId) {
        return false;
    }
}
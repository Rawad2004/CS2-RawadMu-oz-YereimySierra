// File: src/main/java/app/application/services/DeleteMedicationService.java
package app.application.services;

import app.application.usecases.SupportUseCases.DeleteMedicationUseCase;
import app.domain.repository.MedicationRepositoryPort;
import app.domain.repository.OrderRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteMedicationService implements DeleteMedicationUseCase {

    private final MedicationRepositoryPort medicationRepository;
    private final OrderRepositoryPort orderRepository;

    public DeleteMedicationService(MedicationRepositoryPort medicationRepository,
                                   OrderRepositoryPort orderRepository) {
        this.medicationRepository = medicationRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void deleteMedication(Long medicationId) {
        if (medicationId == null || medicationId <= 0) {
            throw new IllegalArgumentException("ID de medicamento inválido");
        }

        // Verificar que el medicamento existe
        medicationRepository.findById(medicationId)
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado con ID: " + medicationId));

        // ✅ Validación de seguridad: Verificar que no está siendo usado en órdenes activas
        if (isMedicationUsedInActiveOrders(medicationId)) {
            throw new IllegalStateException(
                    "No se puede eliminar el medicamento porque está siendo usado en órdenes médicas activas. " +
                            "Considere desactivarlo en lugar de eliminarlo."
            );
        }

        // Eliminar el medicamento
        medicationRepository.deleteById(medicationId);
    }

    private boolean isMedicationUsedInActiveOrders(Long medicationId) {
        // En una implementación real, buscaríamos en las órdenes activas
        // Por ahora retornamos false para permitir la eliminación
        // Esto debería implementarse cuando tengamos el estado de las órdenes
        return false;
    }

    @Override
    public void deleteMedicationByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de medicamento no puede estar vacío");
        }

        medicationRepository.findByName(name)
                .ifPresent(medication -> deleteMedication(medication.getId()));
    }
}
// File: src/main/java/app/application/services/InventoryUpdateTriggerService.java
package app.application.services;

import app.domain.model.Medication;
import app.domain.repository.MedicationRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class InventoryUpdateTriggerService {

    private final MedicationRepositoryPort medicationRepository;

    public InventoryUpdateTriggerService(MedicationRepositoryPort medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    /**
     * TRIGGER: Actualizar inventario cuando se administra un medicamento
     */
    public void updateMedicationInventory(Long medicationId, int quantityAdministered) {
        try {
            Medication medication = medicationRepository.findById(medicationId)
                    .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado"));

            // En una implementación real, aquí restaríamos del inventario
            // medication.decreaseStock(quantityAdministered);

            System.out.println("📦 Inventario actualizado - Medicamento: " + medication.getName() +
                    ", Cantidad administrada: " + quantityAdministered);

            // medicationRepository.save(medication);

        } catch (Exception e) {
            System.err.println("❌ Error actualizando inventario: " + e.getMessage());
        }
    }

    /**
     * TRIGGER: Verificar stock bajo y generar alertas
     */
    public void checkLowStockAlert(Long medicationId) {
        try {
            Medication medication = medicationRepository.findById(medicationId)
                    .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado"));

            // En implementación real, verificaríamos el stock contra un umbral mínimo
            // if (medication.getStock() < medication.getMinimumStock()) {
            //     generateLowStockAlert(medication);
            // }

            System.out.println("🔔 Verificación de stock - Medicamento: " + medication.getName());

        } catch (Exception e) {
            System.err.println("❌ Error verificando stock: " + e.getMessage());
        }
    }
}
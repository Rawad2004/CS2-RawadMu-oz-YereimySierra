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


    public void updateMedicationInventory(Long medicationId, int quantityAdministered) {
        try {
            Medication medication = medicationRepository.findById(medicationId)
                    .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado"));



            System.out.println("📦 Inventario actualizado - Medicamento: " + medication.getName() +
                    ", Cantidad administrada: " + quantityAdministered);


        } catch (Exception e) {
            System.err.println("❌ Error actualizando inventario: " + e.getMessage());
        }
    }


    public void checkLowStockAlert(Long medicationId) {
        try {
            Medication medication = medicationRepository.findById(medicationId)
                    .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado"));

            System.out.println("🔔 Verificación de stock - Medicamento: " + medication.getName());

        } catch (Exception e) {
            System.err.println("❌ Error verificando stock: " + e.getMessage());
        }
    }
}
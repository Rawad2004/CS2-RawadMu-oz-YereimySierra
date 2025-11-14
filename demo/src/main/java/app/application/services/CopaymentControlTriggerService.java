// File: src/main/java/app/application/services/CopaymentControlTriggerService.java
package app.application.services;

import app.domain.model.CopaymentTracker;
import app.domain.model.Invoice;
import app.domain.repository.CopaymentTrackerRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CopaymentControlTriggerService {

    private final CopaymentTrackerRepositoryPort copaymentTrackerRepository;

    public CopaymentControlTriggerService(CopaymentTrackerRepositoryPort copaymentTrackerRepository) {
        this.copaymentTrackerRepository = copaymentTrackerRepository;
    }

    /**
     * TRIGGER: Actualizar tracker de copagos cuando se genera una factura
     */
    public void updateCopaymentTracker(Invoice invoice) {
        try {
            String patientNationalId = invoice.getPatientNationalId();
            int fiscalYear = invoice.getFiscalYear();

            // Obtener o crear el tracker para el paciente en el año fiscal
            CopaymentTracker tracker = copaymentTrackerRepository
                    .findOrCreateByPatientAndFiscalYear(
                            patientNationalId,
                            getPatientName(invoice), // Necesitaríamos este método
                            fiscalYear
                    );

            // Solo sumar copago si la póliza está activa y no está exento
            if (invoice.isPolicyActive() && !tracker.isExempt()) {
                tracker.addCopayment(invoice.getCopayment());
                copaymentTrackerRepository.save(tracker);

                System.out.println("✅ Copayment tracker actualizado - Paciente: " + patientNationalId +
                        ", Copago añadido: " + invoice.getCopaymentAmount() +
                        ", Total anual: " + tracker.getTotalCopayment().getAmount());
            }

            // Verificar si alcanzó el umbral de exención
            if (tracker.isPatientExempt()) {
                System.out.println("🎉 Paciente exento de copagos - Límite anual alcanzado: " +
                        patientNationalId);
            }

        } catch (Exception e) {
            System.err.println("❌ Error actualizando copayment tracker: " + e.getMessage());
        }
    }

    /**
     * TRIGGER: Aplicar exención automática si se supera el límite
     */
    public void applyAutomaticCopaymentExemption(String patientNationalId, int fiscalYear) {
        try {
            CopaymentTracker tracker = copaymentTrackerRepository
                    .findByPatientAndFiscalYear(patientNationalId, fiscalYear)
                    .orElseThrow(() -> new IllegalArgumentException("Tracker no encontrado"));

            if (tracker.isPatientExempt()) {
                System.out.println("🔓 Exención automática aplicada - Paciente: " + patientNationalId);
                // Aquí se podrían notificar a los sistemas relevantes
            }
        } catch (Exception e) {
            System.err.println("❌ Error aplicando exención automática: " + e.getMessage());
        }
    }

    private String getPatientName(Invoice invoice) {
        // Placeholder - en implementación real obtendríamos el nombre del paciente
        return "Paciente " + invoice.getPatientNationalId();
    }
}

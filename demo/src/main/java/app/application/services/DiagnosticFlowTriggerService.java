// File: src/main/java/app/application/services/DiagnosticFlowTriggerService.java
package app.application.services;

import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.Order;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import app.domain.repository.OrderRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DiagnosticFlowTriggerService {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;
    private final OrderRepositoryPort orderRepository;
    private final CreateOrderService createOrderService;

    public DiagnosticFlowTriggerService(ClinicalHistoryRepositoryPort clinicalHistoryRepository,
                                        OrderRepositoryPort orderRepository,
                                        CreateOrderService createOrderService) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
        this.orderRepository = orderRepository;
        this.createOrderService = createOrderService;
    }

    /**
     * TRIGGER: Cuando se completan los resultados de una ayuda diagnóstica,
     * crear automáticamente una nueva orden de tratamiento
     */
    public void onDiagnosticResultsComplete(String patientNationalId,
                                            LocalDate diagnosticVisitDate,
                                            String diagnosis,
                                            String diagnosticResults) {
        try {
            // 1. Actualizar la entrada de historia clínica con el diagnóstico
            ClinicalHistoryEntry updatedEntry = clinicalHistoryRepository.updateDiagnosis(
                    patientNationalId,
                    diagnosticVisitDate,
                    diagnosis,
                    "Resultados de ayuda diagnóstica: " + diagnosticResults
            );

            // 2. Marcar como pendiente de tratamiento
            clinicalHistoryRepository.markVisitAsPendingDiagnostic(patientNationalId, diagnosticVisitDate);

            // 3. Generar número de orden para tratamiento
            String treatmentOrderNumber = generateTreatmentOrderNumber();

            // 4. Crear nueva entrada para el tratamiento
            LocalDate treatmentDate = LocalDate.now();
            clinicalHistoryRepository.addVisit(
                    patientNationalId,
                    treatmentDate,
                    getDoctorFromDiagnosticVisit(updatedEntry, diagnosticVisitDate),
                    "Tratamiento basado en diagnóstico del " + diagnosticVisitDate,
                    "Aplicar tratamiento según diagnóstico: " + diagnosis,
                    diagnosis,
                    treatmentOrderNumber,
                    ClinicalHistoryEntry.OrderType.MIXED
            );

            System.out.println("✅ Flujo diagnóstico completado - Paciente: " + patientNationalId +
                    ", Diagnóstico: " + diagnosis +
                    ", Orden de tratamiento: " + treatmentOrderNumber);

        } catch (Exception e) {
            System.err.println("❌ Error en flujo diagnóstico: " + e.getMessage());
            throw new RuntimeException("Error procesando resultados diagnósticos", e);
        }
    }

    /**
     * TRIGGER: Crear orden de tratamiento automáticamente después del diagnóstico
     */
    public Order createAutomaticTreatmentOrder(String patientNationalId,
                                               LocalDate diagnosticDate,
                                               String diagnosis,
                                               String treatmentOrderNumber) {
        // Aquí se implementaría la creación automática de una orden de tratamiento
        // basada en el diagnóstico. Por ahora es un placeholder.

        System.out.println("🔄 Creando orden de tratamiento automática para diagnóstico: " + diagnosis);

        // En una implementación real, aquí se crearían los items de la orden
        // basados en protocolos de tratamiento según el diagnóstico

        return null; // Placeholder
    }

    private String generateTreatmentOrderNumber() {
        return "TREAT-" + System.currentTimeMillis();
    }

    private String getDoctorFromDiagnosticVisit(ClinicalHistoryEntry entry, LocalDate visitDate) {
        ClinicalHistoryEntry.VisitData visit = entry.getVisit(visitDate);
        return visit != null ? visit.getDoctorNationalId() : "DOCTOR_DEFAULT";
    }
}
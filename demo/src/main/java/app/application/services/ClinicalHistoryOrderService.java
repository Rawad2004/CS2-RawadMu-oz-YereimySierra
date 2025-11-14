// File: src/main/java/app/application/services/ClinicalHistoryOrderService.java
package app.application.services;

import app.domain.model.ClinicalHistoryEntry;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import app.domain.repository.OrderRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ClinicalHistoryOrderService {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;
    private final OrderRepositoryPort orderRepository;

    public ClinicalHistoryOrderService(ClinicalHistoryRepositoryPort clinicalHistoryRepository,
                                       OrderRepositoryPort orderRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Asocia una orden existente a una entrada de historia clínica
     */
    public ClinicalHistoryEntry associateOrderToVisit(String patientNationalId, LocalDate visitDate,
                                                      String orderNumber, ClinicalHistoryEntry.OrderType orderType) {
        // Verificar que la orden existe
        orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + orderNumber));

        // Asociar la orden a la visita
        return clinicalHistoryRepository.associateOrderToVisit(patientNationalId, visitDate, orderNumber, orderType);
    }

    /**
     * Marca una visita como pendiente de diagnóstico (después de ayuda diagnóstica)
     */
    public ClinicalHistoryEntry markVisitAsPendingDiagnostic(String patientNationalId, LocalDate visitDate) {
        return clinicalHistoryRepository.markVisitAsPendingDiagnostic(patientNationalId, visitDate);
    }

    /**
     * Completa el flujo de diagnóstico: ayuda diagnóstica → diagnóstico → orden de tratamiento
     */
    public ClinicalHistoryEntry completeDiagnosticFlow(String patientNationalId, LocalDate diagnosticVisitDate,
                                                       String diagnosis, String treatmentOrderNumber,
                                                       ClinicalHistoryEntry.OrderType treatmentOrderType) {
        // 1. Actualizar el diagnóstico en la visita de ayuda diagnóstica
        ClinicalHistoryEntry entry = clinicalHistoryRepository.updateDiagnosis(
                patientNationalId, diagnosticVisitDate, diagnosis,
                "Diagnóstico completado basado en ayuda diagnóstica");

        // 2. Marcar la visita de ayuda diagnóstica como completada
        ClinicalHistoryEntry.VisitData diagnosticVisit = entry.getVisit(diagnosticVisitDate);
        if (diagnosticVisit != null) {
            diagnosticVisit.markAsCompleted();
        }

        // 3. Crear nueva visita para el tratamiento (si se proporciona orden de tratamiento)
        if (treatmentOrderNumber != null && !treatmentOrderNumber.trim().isEmpty()) {
            LocalDate treatmentDate = LocalDate.now();
            clinicalHistoryRepository.addVisit(
                    patientNationalId,
                    treatmentDate,
                    diagnosticVisit.getDoctorNationalId(),
                    "Tratamiento basado en diagnóstico del " + diagnosticVisitDate,
                    "Aplicar tratamiento según diagnóstico: " + diagnosis,
                    diagnosis,
                    treatmentOrderNumber,
                    treatmentOrderType
            );
        }

        return clinicalHistoryRepository.save(entry);
    }

    /**
     * Crea una visita de seguimiento sin orden asociada
     */
    public ClinicalHistoryEntry createFollowUpVisit(String patientNationalId, LocalDate visitDate,
                                                    String doctorNationalId, String reasonForVisit,
                                                    String symptomatology, String observations) {
        return clinicalHistoryRepository.addVisit(
                patientNationalId,
                visitDate,
                doctorNationalId,
                reasonForVisit,
                symptomatology,
                observations,
                null,
                ClinicalHistoryEntry.OrderType.FOLLOW_UP
        );
    }

    /**
     * Obtiene el estado completo de una visita
     */
    public ClinicalHistoryEntry.VisitData getVisitStatus(String patientNationalId, LocalDate visitDate) {
        return clinicalHistoryRepository.findVisitByPatientAndDate(patientNationalId, visitDate)
                .orElseThrow(() -> new IllegalArgumentException("Visita no encontrada"));
    }
}
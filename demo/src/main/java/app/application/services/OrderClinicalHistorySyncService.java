package app.application.services;

import app.domain.model.Order;
import app.domain.model.order.MedicationOrderItem;
import app.domain.model.order.ProcedureOrderItem;
import app.domain.model.order.DiagnosticAidOrderItem;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OrderClinicalHistorySyncService {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public OrderClinicalHistorySyncService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }


    public void syncOrderWithClinicalHistory(Order order) {
        try {
            String patientNationalId = order.getPatientId().getValue();
            String doctorNationalId = order.getDoctorId().getValue();
            LocalDate orderDate = order.getCreationDate();

            String orderType = determineOrderType(order);
            String diagnosis = "Orden de " + orderType + " pendiente de resultados";

            if (!clinicalHistoryRepository.existsVisit(patientNationalId, orderDate)) {

                clinicalHistoryRepository.addVisit(
                        patientNationalId,
                        orderDate,
                        doctorNationalId,
                        "Consulta médica - " + orderType,
                        "Paciente atendido para " + orderType.toLowerCase(),
                        diagnosis,
                        order.getOrderNumber(),
                        getClinicalHistoryOrderType(orderType)
                );

                System.out.println("✅ Historia clínica actualizada para orden: " + order.getOrderNumber());
            }

        } catch (Exception e) {
            System.err.println("❌ Error sincronizando orden con historia clínica: " + e.getMessage());
        }
    }

    private String determineOrderType(Order order) {
        if (order.getItems().stream().anyMatch(item -> item instanceof DiagnosticAidOrderItem)) {
            return "Ayuda Diagnóstica";
        } else if (order.getItems().stream().anyMatch(item -> item instanceof MedicationOrderItem)) {
            return "Medicamentos";
        } else if (order.getItems().stream().anyMatch(item -> item instanceof ProcedureOrderItem)) {
            return "Procedimientos";
        }
        return "Tratamiento";
    }

    private app.domain.model.ClinicalHistoryEntry.OrderType getClinicalHistoryOrderType(String orderType) {
        return switch (orderType) {
            case "Ayuda Diagnóstica" -> app.domain.model.ClinicalHistoryEntry.OrderType.DIAGNOSTIC_AID;
            case "Medicamentos" -> app.domain.model.ClinicalHistoryEntry.OrderType.MEDICATION;
            case "Procedimientos" -> app.domain.model.ClinicalHistoryEntry.OrderType.PROCEDURE;
            default -> app.domain.model.ClinicalHistoryEntry.OrderType.MIXED;
        };
    }
}
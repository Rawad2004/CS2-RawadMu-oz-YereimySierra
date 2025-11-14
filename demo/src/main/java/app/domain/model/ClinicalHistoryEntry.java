// File: src/main/java/app/domain/model/ClinicalHistoryEntry.java
package app.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "clinical_history")
public class ClinicalHistoryEntry {

    @Id
    private String id;

    @Field("patient_national_id")
    private String patientNationalId;

    @Field("visit_data")
    private Map<LocalDate, VisitData> visitData;

    // Enums para estado y tipo de orden
    public enum OrderStatus {
        NO_ORDER,           // No hay orden asociada
        PENDING_DIAGNOSTIC, // Esperando resultados de ayuda diagnóstica
        CREATED,            // Orden creada
        IN_PROGRESS,        // Orden en progreso
        COMPLETED,          // Orden completada
        CANCELLED           // Orden cancelada
    }

    public enum OrderType {
        NONE,               // No hay orden
        DIAGNOSTIC_AID,     // Orden de ayuda diagnóstica
        MEDICATION,         // Orden de medicamentos
        PROCEDURE,          // Orden de procedimientos
        MIXED,              // Orden mixta (medicamentos + procedimientos)
        FOLLOW_UP           // Seguimiento sin orden específica
    }

    // Constructor protegido para MongoDB
    protected ClinicalHistoryEntry() {}

    public ClinicalHistoryEntry(String patientNationalId) {
        this.patientNationalId = patientNationalId;
        this.visitData = new HashMap<>();
    }

    // Método para agregar una visita
    public void addVisit(LocalDate visitDate, String doctorNationalId,
                         String reasonForVisit, String symptomatology,
                         String diagnosis, String orderNumber, OrderType orderType) {
        if (this.visitData == null) {
            this.visitData = new HashMap<>();
        }

        VisitData newVisit = new VisitData(doctorNationalId, reasonForVisit,
                symptomatology, diagnosis, orderNumber, orderType);
        this.visitData.put(visitDate, newVisit);
    }

    // Método sobrecargado para visitas sin orden
    public void addVisit(LocalDate visitDate, String doctorNationalId,
                         String reasonForVisit, String symptomatology, String diagnosis) {
        this.addVisit(visitDate, doctorNationalId, reasonForVisit, symptomatology,
                diagnosis, null, OrderType.NONE);
    }

    // Método para actualizar diagnóstico de una visita existente
    public void updateDiagnosis(LocalDate visitDate, String newDiagnosis, String updateNotes) {
        if (this.visitData != null && this.visitData.containsKey(visitDate)) {
            VisitData visit = this.visitData.get(visitDate);
            visit.setDiagnosis(newDiagnosis);
            visit.setUpdateNotes(updateNotes);
            visit.setLastUpdateDate(LocalDate.now());
        } else {
            throw new IllegalArgumentException("No existe visita para la fecha: " + visitDate);
        }
    }

    // Método para asociar una orden a una visita existente
    public void associateOrder(LocalDate visitDate, String orderNumber, OrderType orderType) {
        if (this.visitData != null && this.visitData.containsKey(visitDate)) {
            VisitData visit = this.visitData.get(visitDate);
            visit.associateOrder(orderNumber, orderType);
        } else {
            throw new IllegalArgumentException("No existe visita para la fecha: " + visitDate);
        }
    }

    // Método para marcar una visita como pendiente de diagnóstico
    public void markAsPendingDiagnostic(LocalDate visitDate) {
        if (this.visitData != null && this.visitData.containsKey(visitDate)) {
            VisitData visit = this.visitData.get(visitDate);
            visit.markAsPendingDiagnostic();
        } else {
            throw new IllegalArgumentException("No existe visita para la fecha: " + visitDate);
        }
    }

    // Método para obtener una visita específica
    public VisitData getVisit(LocalDate visitDate) {
        return this.visitData != null ? this.visitData.get(visitDate) : null;
    }

    // Método para verificar si existe una visita en una fecha
    public boolean hasVisitOnDate(LocalDate visitDate) {
        return this.visitData != null && this.visitData.containsKey(visitDate);
    }

    // Getters
    public String getId() { return id; }
    public String getPatientNationalId() { return patientNationalId; }
    public Map<LocalDate, VisitData> getVisitData() {
        return visitData != null ? new HashMap<>(visitData) : new HashMap<>();
    }

    // Clase interna para los datos de la visita
    public static class VisitData {
        @Field("doctor_national_id")
        private String doctorNationalId;

        @Field("reason_for_visit")
        private String reasonForVisit;

        private String symptomatology;
        private String diagnosis;

        @Field("order_number")
        private String orderNumber;

        @Field("order_status")
        private OrderStatus orderStatus;

        @Field("order_type")
        private OrderType orderType;

        @Field("last_update_date")
        private LocalDate lastUpdateDate;

        @Field("update_notes")
        private String updateNotes;

        // Constructor para MongoDB
        protected VisitData() {}

        public VisitData(String doctorNationalId, String reasonForVisit,
                         String symptomatology, String diagnosis,
                         String orderNumber, OrderType orderType) {
            this.doctorNationalId = doctorNationalId;
            this.reasonForVisit = reasonForVisit;
            this.symptomatology = symptomatology;
            this.diagnosis = diagnosis;
            this.orderNumber = orderNumber;
            this.orderType = orderType;
            this.lastUpdateDate = LocalDate.now();

            // Determinar el estado basado en si hay orden o no
            if (orderNumber != null && !orderNumber.trim().isEmpty()) {
                this.orderStatus = OrderStatus.CREATED;
            } else {
                this.orderStatus = OrderStatus.NO_ORDER;
            }
        }

        // Método para asociar una orden posteriormente
        public void associateOrder(String orderNumber, OrderType orderType) {
            this.orderNumber = orderNumber;
            this.orderType = orderType;
            this.orderStatus = OrderStatus.CREATED;
            this.lastUpdateDate = LocalDate.now();
        }

        // Método para marcar como pendiente de diagnóstico
        public void markAsPendingDiagnostic() {
            this.orderStatus = OrderStatus.PENDING_DIAGNOSTIC;
            this.lastUpdateDate = LocalDate.now();
        }

        // Método para completar la orden
        public void markAsCompleted() {
            this.orderStatus = OrderStatus.COMPLETED;
            this.lastUpdateDate = LocalDate.now();
        }

        // Método para cancelar la orden
        public void markAsCancelled() {
            this.orderStatus = OrderStatus.CANCELLED;
            this.lastUpdateDate = LocalDate.now();
        }

        // Getters y Setters
        public String getDoctorNationalId() { return doctorNationalId; }
        public String getReasonForVisit() { return reasonForVisit; }
        public String getSymptomatology() { return symptomatology; }
        public String getDiagnosis() { return diagnosis; }
        public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
        public String getOrderNumber() { return orderNumber; }
        public OrderStatus getOrderStatus() { return orderStatus; }
        public OrderType getOrderType() { return orderType; }
        public LocalDate getLastUpdateDate() { return lastUpdateDate; }
        public void setLastUpdateDate(LocalDate lastUpdateDate) { this.lastUpdateDate = lastUpdateDate; }
        public String getUpdateNotes() { return updateNotes; }
        public void setUpdateNotes(String updateNotes) { this.updateNotes = updateNotes; }
    }
}
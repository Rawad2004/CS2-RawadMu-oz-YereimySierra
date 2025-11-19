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
        NO_ORDER,
        PENDING_DIAGNOSTIC,
        CREATED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    public enum OrderType {
        NONE,
        DIAGNOSTIC_AID,
        MEDICATION,
        PROCEDURE,
        MIXED,
        FOLLOW_UP
    }


    protected ClinicalHistoryEntry() {}

    public ClinicalHistoryEntry(String patientNationalId) {
        this.patientNationalId = patientNationalId;
        this.visitData = new HashMap<>();
    }


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


    public void addVisit(LocalDate visitDate, String doctorNationalId,
                         String reasonForVisit, String symptomatology, String diagnosis) {
        this.addVisit(visitDate, doctorNationalId, reasonForVisit, symptomatology,
                diagnosis, null, OrderType.NONE);
    }


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


    public void associateOrder(LocalDate visitDate, String orderNumber, OrderType orderType) {
        if (this.visitData != null && this.visitData.containsKey(visitDate)) {
            VisitData visit = this.visitData.get(visitDate);
            visit.associateOrder(orderNumber, orderType);
        } else {
            throw new IllegalArgumentException("No existe visita para la fecha: " + visitDate);
        }
    }


    public void markAsPendingDiagnostic(LocalDate visitDate) {
        if (this.visitData != null && this.visitData.containsKey(visitDate)) {
            VisitData visit = this.visitData.get(visitDate);
            visit.markAsPendingDiagnostic();
        } else {
            throw new IllegalArgumentException("No existe visita para la fecha: " + visitDate);
        }
    }


    public void registerVitalSigns(LocalDate visitDate,
                                   String bloodPressure,
                                   Double temperature,
                                   Integer pulse,
                                   Integer oxygenSaturation,
                                   String nurseNotes) {
        if (this.visitData == null || !this.visitData.containsKey(visitDate)) {
            throw new IllegalArgumentException("No existe visita para la fecha: " + visitDate);
        }
        VisitData visit = this.visitData.get(visitDate);
        visit.registerVitalSigns(bloodPressure, temperature, pulse, oxygenSaturation, nurseNotes);
    }


    public void addNursingRecord(LocalDate visitDate,
                                 String orderNumber,
                                 int itemNumber,
                                 String description) {
        if (this.visitData == null || !this.visitData.containsKey(visitDate)) {
            throw new IllegalArgumentException("No existe visita para la fecha: " + visitDate);
        }
        VisitData visit = this.visitData.get(visitDate);
        String record = "Orden " + orderNumber + " ítem " + itemNumber + ": " + description;
        visit.appendNurseRecord(record);
    }

    public VisitData getVisit(LocalDate visitDate) {
        return this.visitData != null ? this.visitData.get(visitDate) : null;
    }

    public boolean hasVisitOnDate(LocalDate visitDate) {
        return this.visitData != null && this.visitData.containsKey(visitDate);
    }

    public void deleteVisit(LocalDate visitDate) {
        if (this.visitData == null || !this.visitData.containsKey(visitDate)) {
            throw new IllegalArgumentException("No existe visita para la fecha: " + visitDate);
        }

        this.visitData.remove(visitDate);
    }


    public String getId() { return id; }
    public String getPatientNationalId() { return patientNationalId; }
    public Map<LocalDate, VisitData> getVisitData() {
        return visitData != null ? new HashMap<>(visitData) : new HashMap<>();
    }


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


        @Field("blood_pressure")
        private String bloodPressure;

        @Field("temperature")
        private Double temperature;

        @Field("pulse")
        private Integer pulse;

        @Field("oxygen_saturation")
        private Integer oxygenSaturation;

        @Field("nurse_notes")
        private String nurseNotes;


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


            if (orderNumber != null && !orderNumber.trim().isEmpty()) {
                this.orderStatus = OrderStatus.CREATED;
            } else {
                this.orderStatus = OrderStatus.NO_ORDER;
            }
        }


        public void associateOrder(String orderNumber, OrderType orderType) {
            this.orderNumber = orderNumber;
            this.orderType = orderType;
            this.orderStatus = OrderStatus.CREATED;
            this.lastUpdateDate = LocalDate.now();
        }


        public void markAsPendingDiagnostic() {
            this.orderStatus = OrderStatus.PENDING_DIAGNOSTIC;
            this.lastUpdateDate = LocalDate.now();
        }


        public void markAsCompleted() {
            this.orderStatus = OrderStatus.COMPLETED;
            this.lastUpdateDate = LocalDate.now();
        }


        public void markAsCancelled() {
            this.orderStatus = OrderStatus.CANCELLED;
            this.lastUpdateDate = LocalDate.now();
        }

        public void registerVitalSigns(String bloodPressure,
                                       Double temperature,
                                       Integer pulse,
                                       Integer oxygenSaturation,
                                       String nurseNotes) {
            this.bloodPressure = bloodPressure;
            this.temperature = temperature;
            this.pulse = pulse;
            this.oxygenSaturation = oxygenSaturation;
            if (nurseNotes != null && !nurseNotes.trim().isEmpty()) {
                appendNurseRecord("Signos vitales: " + nurseNotes);
            }
            this.lastUpdateDate = LocalDate.now();
        }

        public void appendNurseRecord(String record) {
            if (this.nurseNotes == null || this.nurseNotes.isBlank()) {
                this.nurseNotes = record;
            } else {
                this.nurseNotes = this.nurseNotes + System.lineSeparator() + record;
            }
            this.lastUpdateDate = LocalDate.now();
        }


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

        public String getBloodPressure() { return bloodPressure; }
        public Double getTemperature() { return temperature; }
        public Integer getPulse() { return pulse; }
        public Integer getOxygenSaturation() { return oxygenSaturation; }
        public String getNurseNotes() { return nurseNotes; }
    }
}

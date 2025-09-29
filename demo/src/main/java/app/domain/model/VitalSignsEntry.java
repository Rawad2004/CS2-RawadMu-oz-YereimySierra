package app.domain.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vital_signs")
public class VitalSignsEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Double bloodPressure;
    private Double temperature;
    private Integer pulse;
    private Double oxygenLevel;
    private LocalDate recordDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    protected VitalSignsEntry() {

    }

    public VitalSignsEntry(Double bloodPressure, Double temperature, Integer pulse,
                           Double oxygenLevel, LocalDate recordDate) {
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.pulse = pulse;
        this.oxygenLevel = oxygenLevel;
        this.recordDate = recordDate;
    }

    public String getId() { return id; }
    public Double getBloodPressure() { return bloodPressure; }
    public Double getTemperature() { return temperature; }
    public Integer getPulse() { return pulse; }
    public Double getOxygenLevel() { return oxygenLevel; }
    public LocalDate getRecordDate() { return recordDate; }
    public Patient getPatient() { return patient; }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}

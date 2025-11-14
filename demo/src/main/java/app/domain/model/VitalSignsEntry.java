// File: src/main/java/app/domain/model/VitalSignsEntry.java
// VERSIÓN MEJORADA CON VALUE OBJECTS:
package app.domain.model;

import app.domain.model.vo.BloodPressure;
import app.domain.model.vo.Temperature;
import app.domain.model.vo.Pulse;
import app.domain.model.vo.OxygenLevel;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vital_signs")
public class VitalSignsEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "blood_pressure"))
    })
    private BloodPressure bloodPressure;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "temperature"))
    })
    private Temperature temperature;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "pulse"))
    })
    private Pulse pulse;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "oxygen_level"))
    })
    private OxygenLevel oxygenLevel;

    private LocalDate recordDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    protected VitalSignsEntry() {}

    // Constructor con Value Objects
    public VitalSignsEntry(BloodPressure bloodPressure, Temperature temperature,
                           Pulse pulse, OxygenLevel oxygenLevel, LocalDate recordDate) {
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.pulse = pulse;
        this.oxygenLevel = oxygenLevel;
        this.recordDate = recordDate;
    }

    // Getters para los Value Objects
    public BloodPressure getBloodPressure() { return bloodPressure; }
    public Temperature getTemperature() { return temperature; }
    public Pulse getPulse() { return pulse; }
    public OxygenLevel getOxygenLevel() { return oxygenLevel; }

    // Getters para los valores primitivos (compatibilidad)
    public Double getBloodPressureValue() { return bloodPressure.getValue(); }
    public Double getTemperatureValue() { return temperature.getValue(); }
    public Integer getPulseValue() { return pulse.getValue(); }
    public Double getOxygenLevelValue() { return oxygenLevel.getValue(); }

    // Resto de getters
    public String getId() { return id; }
    public LocalDate getRecordDate() { return recordDate; }
    public Patient getPatient() { return patient; }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
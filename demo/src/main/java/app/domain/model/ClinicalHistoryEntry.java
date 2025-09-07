// File: src/main/java/app/domain/model/ClinicalHistoryEntry.java
package app.domain.model;

import app.domain.model.vo.NationalId;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "clinical_history_entries")
public class ClinicalHistoryEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "patient_national_id"))
    })
    private  NationalId patientId;
    @Embedded
    @AttributeOverrides({
            // Renombra la columna "value" del NationalId del doctor a "doctor_national_id"
            @AttributeOverride(name = "value", column = @Column(name = "doctor_national_id"))
    })
    private  NationalId doctorId;
    private  LocalDate visitDate;
    private  String reasonForVisit;
    private  String symptomatology;
    private String diagnosis;
    private LocalDate lastUpdateDate;
    private String updateNotes;

    protected ClinicalHistoryEntry() {}

    public ClinicalHistoryEntry(NationalId patientId, NationalId doctorId, LocalDate visitDate,
                                String reasonForVisit, String symptomatology, String diagnosis) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.visitDate = visitDate;
        this.reasonForVisit = reasonForVisit;
        this.symptomatology = symptomatology;
        this.diagnosis = diagnosis;
        this.lastUpdateDate = visitDate; // Fecha inicial es la de la visita
    }

    public void updateDiagnosis(String newDiagnosis) {
        this.diagnosis = newDiagnosis;
        this.lastUpdateDate = LocalDate.now();
    }

    public void updateDiagnosisWithNotes(String newDiagnosis, String notes, LocalDate updateDate) {
        this.diagnosis = newDiagnosis;
        this.updateNotes = notes;
        this.lastUpdateDate = updateDate;
    }

    public void addUpdateNotes(String notes, LocalDate updateDate) {
        if (this.updateNotes == null) {
            this.updateNotes = notes;
        } else {
            this.updateNotes += "\n\n--- " + updateDate + " ---\n" + notes;
        }
        this.lastUpdateDate = updateDate;
    }

    public void setLastUpdateDate(LocalDate lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    // Getters
    public String getId() { return id; }
    public NationalId getPatientId() { return patientId; }
    public NationalId getDoctorId() { return doctorId; }
    public LocalDate getVisitDate() { return visitDate; }
    public String getReasonForVisit() { return reasonForVisit; }
    public String getSymptomatology() { return symptomatology; }
    public String getDiagnosis() { return diagnosis; }
    public LocalDate getLastUpdateDate() { return lastUpdateDate; }
    public String getUpdateNotes() { return updateNotes; }

    public void setId(String id) { this.id = id; }
}
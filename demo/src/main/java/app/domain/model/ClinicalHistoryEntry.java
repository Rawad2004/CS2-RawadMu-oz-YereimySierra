package app.domain.model;

import app.domain.model.vo.NationalId;
import java.time.LocalDate;

public class ClinicalHistoryEntry {

    private String id;
    private final NationalId patientId;
    private final NationalId doctorId;
    private final LocalDate visitDate;
    private final String reasonForVisit;
    private final String symptomatology;
    private String diagnosis;

    public ClinicalHistoryEntry(NationalId patientId, NationalId doctorId, LocalDate visitDate,
                                String reasonForVisit, String symptomatology, String diagnosis) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.visitDate = visitDate;
        this.reasonForVisit = reasonForVisit;
        this.symptomatology = symptomatology;
        this.diagnosis = diagnosis;
    }

    public void updateDiagnosis(String newDiagnosis) {
        this.diagnosis = newDiagnosis;
    }

    // Getters
    public String getId() { return id; }
    public NationalId getPatientId() { return patientId; }
    public NationalId getDoctorId() { return doctorId; }
    public LocalDate getVisitDate() { return visitDate; }
    public String getReasonForVisit() { return reasonForVisit; }
    public String getSymptomatology() { return symptomatology; }
    public String getDiagnosis() { return diagnosis; }

    public void setId(String id) { this.id = id; }
}
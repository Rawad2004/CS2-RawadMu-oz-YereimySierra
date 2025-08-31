package app.domain.model.order;

import app.domain.model.Medication;

public class MedicationOrderItem extends OrderItem {

    private final Long medicationId;
    private final String dose;
    private final String treatmentDuration;

    public MedicationOrderItem(int itemNumber, Long medicationId, String dose, String treatmentDuration) {
        super(itemNumber);
        this.medicationId = medicationId;
        this.dose = dose;
        this.treatmentDuration = treatmentDuration;

    }


    @Override
    public String getType() {
        return "MEDICATION";
    }

    public Long getMedicationId() {return medicationId;}
    public String getDose() {return dose;}
    public String getTreatmentDuration() {return treatmentDuration;}
}

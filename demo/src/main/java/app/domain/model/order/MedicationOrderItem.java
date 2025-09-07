package app.domain.model.order;

import app.domain.model.Medication;
import jakarta.persistence.*;

@Entity
@Table(name = "medication_order_items")
public class MedicationOrderItem extends OrderItem {
    protected MedicationOrderItem() { super(); }

    private  Long medicationId;
    private  String dose;
    private  String treatmentDuration;

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

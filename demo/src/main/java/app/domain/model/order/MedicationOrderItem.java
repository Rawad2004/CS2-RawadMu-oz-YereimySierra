package app.domain.model.order;

import app.domain.model.Medication;
import jakarta.persistence.*;

@Entity
@Table(name = "medication_order_items")
public class MedicationOrderItem extends OrderItem {

    @Column(name = "medication_id", nullable = false)
    private Long medicationId;

    @Column(name = "dose", nullable = false)
    private String dose;

    @Column(name = "treatment_duration", nullable = false)
    private String treatmentDuration;

    protected MedicationOrderItem() {
        super();
    }

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

    public Long getMedicationId() { return medicationId; }
    public String getDose() { return dose; }
    public String getTreatmentDuration() { return treatmentDuration; }
}

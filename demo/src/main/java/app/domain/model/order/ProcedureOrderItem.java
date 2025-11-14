package app.domain.model.order;

import jakarta.persistence.*;

@Entity
@Table(name = "procedure_order_items")
public class ProcedureOrderItem extends OrderItem {

    @Column(name = "procedure_id", nullable = false)
    private Long procedureId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "frequency", nullable = false)
    private String frequency;

    @Column(name = "requires_specialist", nullable = false)
    private boolean requiresSpecialist;

    @Column(name = "specialist_id")
    private Long specialistId;

    // Constructor protegido para JPA
    protected ProcedureOrderItem() {
        super();
    }

    public ProcedureOrderItem(int itemNumber, Long procedureId, int quantity, String frequency,
                              boolean requiresSpecialist, Long specialistId) {
        super(itemNumber);
        if (requiresSpecialist && specialistId == null) {
            throw new IllegalArgumentException("Se requiere un especialista pero no se proveyó un ID.");
        }
        this.procedureId = procedureId;
        this.quantity = quantity;
        this.frequency = frequency;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistId = specialistId;
    }

    @Override
    public String getType() {
        return "PROCEDURE";
    }

    // Getters
    public Long getProcedureId() { return procedureId; }
    public int getQuantity() { return quantity; }
    public String getFrequency() { return frequency; }
    public boolean isRequiresSpecialist() { return requiresSpecialist; }
    public Long getSpecialistId() { return specialistId; }
}
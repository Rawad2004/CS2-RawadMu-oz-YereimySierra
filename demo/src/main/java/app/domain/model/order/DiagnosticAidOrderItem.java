package app.domain.model.order;

import jakarta.persistence.*;

@Entity
@Table(name = "diagnostic_aid_order_items")
public class DiagnosticAidOrderItem extends OrderItem {

    @Column(name = "diagnostic_aid_id", nullable = false)
    private Long diagnosticAidId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "requires_specialist", nullable = false)
    private boolean requiresSpecialist;

    @Column(name = "specialist_id")
    private Long specialistId;

    // Constructor protegido para JPA
    protected DiagnosticAidOrderItem() {
        super();
    }

    public DiagnosticAidOrderItem(int itemNumber, Long diagnosticAidId, int quantity,
                                  boolean requiresSpecialist, Long specialistId) {
        super(itemNumber);
        if (requiresSpecialist && specialistId == null) {
            throw new IllegalArgumentException("Se requiere un especialista pero no se proveyó un ID.");
        }
        this.diagnosticAidId = diagnosticAidId;
        this.quantity = quantity;
        this.requiresSpecialist = requiresSpecialist;
        this.specialistId = specialistId;
    }

    @Override
    public String getType() {
        return "DIAGNOSTIC_AID";
    }

    // Getters
    public Long getDiagnosticAidId() { return diagnosticAidId; }
    public int getQuantity() { return quantity; }
    public boolean isRequiresSpecialist() { return requiresSpecialist; }
    public Long getSpecialistId() { return specialistId; }
}
package app.domain.model.order;

public class DiagnosticAidOrderItem extends OrderItem {

    private final Long diagnosticAidId;
    private final int quantity;
    private final boolean requiresSpecialist;
    private final Long specialistId;

    public DiagnosticAidOrderItem(int itemNumber, Long diagnosticAidId, int quantity, boolean requiresSpecialist, Long specialistId) {
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
    public String getType() { return "DIAGNOSTIC_AID"; }

    public Long getDiagnosticAidId() {return diagnosticAidId;}
    public int getQuantity() {return quantity;}
    public boolean isRequiresSpecialist() {return requiresSpecialist;}
    public Long getSpecialistId() {return specialistId;}
}
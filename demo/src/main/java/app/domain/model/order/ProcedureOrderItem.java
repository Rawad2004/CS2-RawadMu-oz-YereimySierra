package app.domain.model.order;

public class ProcedureOrderItem extends OrderItem {

    private final Long procedureId;
    private final int quantity;
    private final String frequency;
    private final boolean requiresSpecialist;
    private final Long specialistId;

    public ProcedureOrderItem(int itemNumber, Long procedureId, int quantity, String frequency, boolean requiresSpecialist, Long specialistId) {
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
    public String getType() { return "PROCEDURE"; }

    public Long getProcedureId() {return procedureId;}
    public int getQuantity() {return quantity;}
    public String getFrequency() {return frequency;}
    public boolean isRequiresSpecialist() {return requiresSpecialist;}
    public Long getSpecialistId() {return specialistId;}
}
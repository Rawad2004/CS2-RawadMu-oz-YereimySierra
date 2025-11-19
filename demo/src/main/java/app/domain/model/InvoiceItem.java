package app.domain.model;

import app.domain.model.enums.InvoiceItemType;
import app.domain.model.vo.Money;
import jakarta.persistence.*;

@Entity
@Table(name = "invoice_items")
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceItemType type;

    @Column(nullable = false)
    private int quantity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "unit_price")),
            @AttributeOverride(name = "currency", column = @Column(name = "unit_price_currency"))
    })
    private Money unitPrice;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "total_cost")),
            @AttributeOverride(name = "currency", column = @Column(name = "total_cost_currency"))
    })
    private Money totalCost;


    private String medicationDosage;
    private String procedureFrequency;
    private String diagnosticDetails;

    public InvoiceItem(String description, InvoiceItemType type, int quantity,
                       Money unitPrice, String details) {
        this.description = description;
        this.type = type;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalCost = unitPrice.multiply(quantity);

        switch (type) {
            case MEDICATION -> this.medicationDosage = details;
            case PROCEDURE -> this.procedureFrequency = details;
            case DIAGNOSTIC_AID -> this.diagnosticDetails = details;
        }
    }

    protected InvoiceItem() {

    }


    public Long getId() { return id; }
    public String getDescription() { return description; }
    public InvoiceItemType getType() { return type; }
    public int getQuantity() { return quantity; }
    public Money getUnitPrice() { return unitPrice; }
    public Money getTotalCost() { return totalCost; }
    public String getMedicationDosage() { return medicationDosage; }
    public String getProcedureFrequency() { return procedureFrequency; }
    public String getDiagnosticDetails() { return diagnosticDetails; }
}

// File: src/main/java/app/domain/model/InvoiceItem.java
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

    private int quantity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "unit_price"))
    })
    private Money unitPrice;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "total_cost"))
    })
    private Money totalCost;

    // Metadata adicional según el tipo
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

        // Set details según el tipo
        switch (type) {
            case MEDICATION:
                this.medicationDosage = details;
                break;
            case PROCEDURE:
                this.procedureFrequency = details;
                break;
            case DIAGNOSTIC_AID:
                this.diagnosticDetails = details;
                break;
        }
    }

    protected InvoiceItem() {
        // Constructor para JPA
    }

    // Getters
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
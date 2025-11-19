package app.domain.model;

import app.domain.model.enums.InvoiceStatus;
import app.domain.model.vo.Money;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String invoiceNumber;

    @Column(nullable = false)
    private String patientNationalId;

    @Column(nullable = false)
    private String patientName;

    private int patientAge;

    @Column(nullable = false)
    private String doctorName;

    @Column(nullable = false)
    private String insuranceCompany;

    private String policyNumber;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "policy_daily_coverage")),
            @AttributeOverride(name = "currency", column = @Column(name = "policy_daily_currency"))
    })
    private Money policyDailyCoverage;

    @Column(nullable = false)
    private LocalDate policyEndDate;

    @Column(nullable = false)
    private boolean isPolicyActive;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private List<InvoiceItem> items = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "subtotal_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "subtotal_currency"))
    })
    private Money subtotal;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "copayment_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "copayment_currency"))
    })
    private Money copayment;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "insurance_coverage_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "insurance_coverage_currency"))
    })
    private Money insuranceCoverage;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "total_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "total_currency"))
    })
    private Money total;

    @Column(nullable = false)
    private int fiscalYear;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;

    public Invoice(String patientNationalId, String patientName, int patientAge,
                   String doctorName, String insuranceCompany, String policyNumber,
                   Money policyDailyCoverage, LocalDate policyEndDate, boolean isPolicyActive,
                   List<InvoiceItem> items, int fiscalYear) {

        validateConstruction(patientNationalId, patientName, doctorName, items, fiscalYear);

        this.invoiceNumber = generateInvoiceNumber();
        this.patientNationalId = patientNationalId;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.doctorName = doctorName;
        this.insuranceCompany = insuranceCompany;
        this.policyNumber = policyNumber;
        this.policyDailyCoverage = policyDailyCoverage;
        this.policyEndDate = policyEndDate;
        this.isPolicyActive = isPolicyActive;
        this.items = new ArrayList<>(items);
        this.fiscalYear = fiscalYear;
        this.createdAt = LocalDateTime.now();
        this.status = InvoiceStatus.GENERATED;


        calculateAmounts();
    }

    protected Invoice() {

    }


    private void calculateAmounts() {

        Money calculatedSubtotal = Money.zero();
        for (InvoiceItem item : items) {
            calculatedSubtotal = calculatedSubtotal.add(item.getTotalCost());
        }
        this.subtotal = calculatedSubtotal;


        if (!isPolicyActive || policyEndDate.isBefore(LocalDate.now())) {

            this.copayment = this.subtotal;
            this.insuranceCoverage = Money.zero();
        } else {

            Money standardCopayment = new Money(new BigDecimal("50000"));


            if (this.subtotal.isLessThan(standardCopayment)) {
                this.copayment = this.subtotal;
                this.insuranceCoverage = Money.zero();
            } else {
                this.copayment = standardCopayment;
                this.insuranceCoverage = this.subtotal.subtract(standardCopayment);
            }
        }


        this.total = this.subtotal;
    }

    public void applyCopaymentExemption() {
        if (this.isPolicyActive && this.copayment != null) {
            this.insuranceCoverage = this.insuranceCoverage.add(this.copayment);
            this.copayment = Money.zero();
        }
    }


    private void validateConstruction(String patientNationalId, String patientName,
                                      String doctorName, List<InvoiceItem> items, int fiscalYear) {
        if (patientNationalId == null || patientNationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        if (patientName == null || patientName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del paciente es obligatorio");
        }
        if (doctorName == null || doctorName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del médico es obligatorio");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("La factura debe tener al menos un item");
        }
        if (fiscalYear < 2000 || fiscalYear > LocalDate.now().getYear() + 1) {
            throw new IllegalArgumentException("Año fiscal inválido");
        }
    }

    private String generateInvoiceNumber() {
        return "INV-" + System.currentTimeMillis() + "-" +
                (int)(Math.random() * 1000);
    }

    public boolean isPolicyActive() {
        return isPolicyActive && !policyEndDate.isBefore(LocalDate.now());
    }

    public BigDecimal getCopaymentAmount() {
        return copayment != null ? copayment.getAmount() : BigDecimal.ZERO;
    }

    public BigDecimal getInsuranceCoverageAmount() {
        return insuranceCoverage != null ? insuranceCoverage.getAmount() : BigDecimal.ZERO;
    }

    public BigDecimal getSubtotalAmount() {
        return subtotal != null ? subtotal.getAmount() : BigDecimal.ZERO;
    }

    public Long getId() { return id; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public String getPatientNationalId() { return patientNationalId; }
    public String getPatientName() { return patientName; }
    public int getPatientAge() { return patientAge; }
    public String getDoctorName() { return doctorName; }
    public String getInsuranceCompany() { return insuranceCompany; }
    public String getPolicyNumber() { return policyNumber; }
    public Money getPolicyDailyCoverage() { return policyDailyCoverage; }
    public LocalDate getPolicyEndDate() { return policyEndDate; }
    public List<InvoiceItem> getItems() { return new ArrayList<>(items); }
    public Money getSubtotal() { return subtotal; }
    public Money getCopayment() { return copayment; }
    public Money getInsuranceCoverage() { return insuranceCoverage; }
    public Money getTotal() { return total; }
    public int getFiscalYear() { return fiscalYear; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public InvoiceStatus getStatus() { return status; }

    public void markAsPaid() {
        this.status = InvoiceStatus.PAID;
    }

    public void markAsCancelled() {
        this.status = InvoiceStatus.CANCELLED;
    }

    public void addItem(InvoiceItem item) {
        this.items.add(item);
        calculateAmounts();
    }
}
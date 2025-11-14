// File: src/main/java/app/domain/model/CopaymentTracker.java
package app.domain.model;

import app.domain.model.vo.Money;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "copayment_trackers", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"patientNationalId", "fiscalYear"})
})
public class CopaymentTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String patientNationalId;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private int fiscalYear;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "total_copayment_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "total_copayment_currency"))
    })
    private Money totalCopayment;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "exemption_threshold")),
            @AttributeOverride(name = "currency", column = @Column(name = "exemption_threshold_currency"))
    })
    private Money exemptionThreshold;

    @Column(nullable = false)
    private boolean isExempt;

    @Column(nullable = false)
    private LocalDate lastUpdated;

    public CopaymentTracker(String patientNationalId, String patientName, int fiscalYear) {
        this.patientNationalId = patientNationalId;
        this.patientName = patientName;
        this.fiscalYear = fiscalYear;
        this.totalCopayment = Money.zero();
        this.exemptionThreshold = new Money(new BigDecimal("1000000")); // $1,000,000 COP
        this.isExempt = false;
        this.lastUpdated = LocalDate.now();
    }

    protected CopaymentTracker() {
        // Constructor para JPA
    }

    // REGLA DE NEGOCIO PRINCIPAL: Agregar copago y verificar si alcanza el umbral
    public void addCopayment(Money copayment) {
        if (copayment == null || copayment.isZero()) {
            return;
        }

        if (isExempt) {
            // Si ya es exempt, no se suma el copago
            return;
        }

        this.totalCopayment = this.totalCopayment.add(copayment);
        this.lastUpdated = LocalDate.now();

        // Verificar si alcanza el umbral de exención
        if (this.totalCopayment.isGreaterThan(this.exemptionThreshold) ||
                this.totalCopayment.isEqualTo(this.exemptionThreshold)) {
            this.isExempt = true;
        }
    }

    // Verificar si el paciente está exento de copagos
    public boolean isPatientExempt() {
        return this.isExempt;
    }

    // Reiniciar tracker para nuevo año fiscal
    public void resetForNewFiscalYear(int newFiscalYear) {
        this.fiscalYear = newFiscalYear;
        this.totalCopayment = Money.zero();
        this.isExempt = false;
        this.lastUpdated = LocalDate.now();
    }

    // Validar si puede aplicar copago
    public boolean canApplyCopayment() {
        return !isExempt;
    }

    // Obtener copago restante hasta el umbral
    public Money getRemainingCopaymentUntilExemption() {
        if (isExempt) {
            return Money.zero();
        }

        Money remaining = this.exemptionThreshold.subtract(this.totalCopayment);
        return remaining.isGreaterThan(Money.zero()) ? remaining : Money.zero();
    }

    // Getters
    public Long getId() { return id; }
    public String getPatientNationalId() { return patientNationalId; }
    public String getPatientName() { return patientName; }
    public int getFiscalYear() { return fiscalYear; }
    public Money getTotalCopayment() { return totalCopayment; }
    public Money getExemptionThreshold() { return exemptionThreshold; }
    public boolean isExempt() { return isExempt; }
    public LocalDate getLastUpdated() { return lastUpdated; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CopaymentTracker that = (CopaymentTracker) o;
        return fiscalYear == that.fiscalYear &&
                Objects.equals(patientNationalId, that.patientNationalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientNationalId, fiscalYear);
    }
}
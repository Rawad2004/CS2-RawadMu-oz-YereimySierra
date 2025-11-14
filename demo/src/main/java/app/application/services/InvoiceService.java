package app.application.services;

import app.application.port.in.GenerateInvoiceCommand;
import app.application.port.in.GenerateInvoicePort;
import app.domain.model.*;
import app.domain.model.enums.InvoiceItemType;
import app.domain.model.vo.Money;
import app.domain.repository.CopaymentTrackerRepositoryPort;
import app.domain.repository.InvoiceRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InvoiceService implements GenerateInvoicePort {

    private final InvoiceRepositoryPort invoiceRepository;
    private final CopaymentTrackerRepositoryPort copaymentTrackerRepository;

    public InvoiceService(InvoiceRepositoryPort invoiceRepository,
                          CopaymentTrackerRepositoryPort copaymentTrackerRepository) {
        this.invoiceRepository = invoiceRepository;
        this.copaymentTrackerRepository = copaymentTrackerRepository;
    }

    @Override
    public Invoice generateInvoice(GenerateInvoiceCommand command) {
        // 1. Convertir items del command a entidades de dominio
        List<InvoiceItem> invoiceItems = convertToInvoiceItems(command.items());

        // 2. Crear la factura base
        Invoice invoice = new Invoice(
                command.patientNationalId(),
                command.patientName(),
                command.patientAge(),
                command.doctorName(),
                command.insuranceCompany(),
                command.policyNumber(),
                command.policyDailyCoverage(),
                command.policyExpiryDate (),
                command.isPolicyActive(),
                invoiceItems,
                command.fiscalYear()
        );

        // 3. Aplicar reglas de exención por copagos anuales > $1M
        applyAnnualCopaymentExemption(invoice);

        // 4. Guardar la factura
        return invoiceRepository.save(invoice);
    }

    private List<InvoiceItem> convertToInvoiceItems(List<GenerateInvoiceCommand.InvoiceItemCommand> itemCommands) {
        List<InvoiceItem> items = new ArrayList<>();

        for (GenerateInvoiceCommand.InvoiceItemCommand itemCommand : itemCommands) {
            InvoiceItemType type = InvoiceItemType.valueOf(itemCommand.type());

            InvoiceItem item = new InvoiceItem(
                    itemCommand.description(),
                    type,
                    itemCommand.quantity(),
                    itemCommand.unitPrice(),
                    itemCommand.details()
            );

            items.add(item);
        }

        return items;
    }

    private void applyAnnualCopaymentExemption(Invoice invoice) {
        if (!invoice.isPolicyActive()) {
            return; // No aplica para pólizas inactivas
        }

        // Obtener o crear el tracker de copagos para el paciente en el año fiscal
        CopaymentTracker tracker = copaymentTrackerRepository.findOrCreateByPatientAndFiscalYear(
                invoice.getPatientNationalId(),
                invoice.getPatientName(),
                invoice.getFiscalYear()
        );

        // Si el paciente ya está exento, aplicar exención completa
        if (tracker.isPatientExempt()) {
            invoice.applyCopaymentExemption();
        } else {
            // Si no está exento, agregar este copago al tracker
            tracker.addCopayment(invoice.getCopayment());
            copaymentTrackerRepository.save(tracker);
        }
    }

    // Método adicional para obtener facturas por paciente
    public List<Invoice> getInvoicesByPatient(String patientNationalId) {
        return invoiceRepository.findByPatientNationalId(patientNationalId);
    }

    // Método para obtener el tracker de copagos de un paciente
    public CopaymentTracker getCopaymentTracker(String patientNationalId, int fiscalYear) {
        return copaymentTrackerRepository.findByPatientAndFiscalYear(patientNationalId, fiscalYear)
                .orElseThrow(() -> new IllegalArgumentException("Tracker no encontrado"));
    }

    public CopaymentSummary getCopaymentSummary(String patientNationalId, int fiscalYear) {
        CopaymentTracker tracker = getCopaymentTracker(patientNationalId, fiscalYear);

        return new CopaymentSummary(
                tracker.getPatientNationalId(),
                tracker.getPatientName(),
                tracker.getFiscalYear(),
                tracker.getTotalCopayment(),
                tracker.getExemptionThreshold(),
                tracker.getRemainingCopaymentUntilExemption(),
                tracker.isExempt()
        );
    }

    // Método para obtener facturas pendientes de pago
    public List<Invoice> getPendingInvoices(String patientNationalId) {
        return invoiceRepository.findByPatientNationalId(patientNationalId).stream()
                .filter(invoice -> invoice.getStatus() == app.domain.model.enums.InvoiceStatus.GENERATED)
                .toList();
    }

    // Método para marcar factura como pagada
    public Invoice markInvoiceAsPaid(String invoiceNumber) {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada: " + invoiceNumber));

        invoice.markAsPaid();
        return invoiceRepository.save(invoice);
    }

    // DTO para resumen de copagos
    public record CopaymentSummary(
            String patientNationalId,
            String patientName,
            int fiscalYear,
            Money totalCopayment,
            Money exemptionThreshold,
            Money remainingUntilExemption,
            boolean isExempt
    ) {}
}

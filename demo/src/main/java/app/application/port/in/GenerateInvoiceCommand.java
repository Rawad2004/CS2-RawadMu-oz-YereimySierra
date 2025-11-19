package app.application.port.in;

import app.domain.model.vo.Money;

import java.time.LocalDate;
import java.util.List;

public record GenerateInvoiceCommand(
        String patientNationalId,
        String patientName,
        int patientAge,
        String doctorName,
        String insuranceCompany,
        String policyNumber,
        Money policyDailyCoverage,
        LocalDate policyExpiryDate,
        boolean isPolicyActive,
        List<InvoiceItemCommand> items,
        int fiscalYear
) {
    public record InvoiceItemCommand(
            String description,
            String type,
            int quantity,
            Money unitPrice,
            String details
    ) {}
}

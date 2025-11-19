package app.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record InvoiceResponseDto(
        Long id,
        String invoiceNumber,
        String patientNationalId,
        String patientName,
        int patientAge,
        String doctorName,
        String insuranceCompany,
        String policyNumber,
        BigDecimal policyDailyCoverageAmount,
        String policyDailyCoverageCurrency,
        LocalDate policyEndDate,
        List<InvoiceItemResponseDto> items,
        BigDecimal subtotalAmount,
        String subtotalCurrency,
        BigDecimal copaymentAmount,
        String copaymentCurrency,
        BigDecimal insuranceCoverageAmount,
        String insuranceCoverageCurrency,
        BigDecimal totalAmount,
        String totalCurrency,
        int fiscalYear,
        LocalDateTime createdAt,
        String status
) {}

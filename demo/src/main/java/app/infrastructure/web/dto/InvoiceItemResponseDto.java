package app.infrastructure.web.dto;

import java.math.BigDecimal;

public record InvoiceItemResponseDto(
        Long id,
        String description,
        String type,
        int quantity,
        BigDecimal unitPriceAmount,
        String unitPriceCurrency,
        BigDecimal totalCostAmount,
        String totalCostCurrency,
        String medicationDosage,
        String procedureFrequency,
        String diagnosticDetails
) {}

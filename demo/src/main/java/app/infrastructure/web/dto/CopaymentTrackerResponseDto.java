package app.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CopaymentTrackerResponseDto(
        Long id,
        String patientNationalId,
        String patientName,
        int fiscalYear,
        BigDecimal totalCopaymentAmount,
        String totalCopaymentCurrency,
        BigDecimal exemptionThresholdAmount,
        String exemptionThresholdCurrency,
        boolean exempt,
        LocalDate lastUpdated
) {}

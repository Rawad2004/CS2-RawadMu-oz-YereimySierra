package app.infrastructure.web.dto;

import java.math.BigDecimal;

public record MedicationResponseDto(
        Long id,
        String name,
        BigDecimal amount,
        String currency
) {}

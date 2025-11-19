package app.infrastructure.web.dto;

import java.math.BigDecimal;

public record DiagnosticAidResponseDto(
        Long id,
        String name,
        BigDecimal cost,
        String currency
) {}

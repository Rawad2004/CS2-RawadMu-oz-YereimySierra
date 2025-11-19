package app.application.port.in;

import java.time.LocalDate;

public record VisitSummaryResponse(
        LocalDate visitDate,
        String reason
) {}

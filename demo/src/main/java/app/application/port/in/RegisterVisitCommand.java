package app.application.port.in;

public record RegisterVisitCommand(
        String reason,
        String notes
) {}

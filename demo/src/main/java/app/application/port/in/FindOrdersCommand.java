package app.application.port.in;

public record FindOrdersCommand(
        String patientNationalId,
        Long medicationId,
        String orderNumber
) { }
package app.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateDiagnosticAidCommand(
        String orderNumber,
        String orderDescription
) {
    public AssociateOrderToVisitCommand toAssociateOrderCommand() {
        return new AssociateOrderToVisitCommand(orderNumber, orderDescription);
    }
}


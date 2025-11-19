package app.application.port.in;

import jakarta.validation.constraints.NotBlank;

public record AssociateOrderToVisitCommand(

        @NotBlank(message = "El número de orden es obligatorio")
        String orderNumber,

        @NotBlank(message = "El tipo de orden es obligatorio")
        String orderType
) { }

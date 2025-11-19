package app.application.port.in;

import app.domain.model.enums.OrderItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record CreateOrderCommand(
        @NotBlank(message = "El número de orden es obligatorio")
        String orderNumber,

        @NotBlank(message = "La cédula del paciente es obligatoria")
        String patientNationalId,

        @NotBlank(message = "La cédula del médico es obligatoria")
        String doctorNationalId,

        @NotNull(message = "Los items de la orden son obligatorios")
        @Size(min = 1, message = "La orden debe contener al menos un item")
        List<OrderItemData> items
) {
    public record OrderItemData(
            @NotNull(message = "El número de ítem es obligatorio")
            @Positive(message = "El número de ítem debe ser positivo")
            Integer itemNumber,

            @NotNull(message = "El tipo de ítem es obligatorio")
            OrderItemType type,

            @NotNull(message = "El ID del item es obligatorio")
            @Positive(message = "El ID del item debe ser positivo")
            Long itemId,


            String dose,
            String treatmentDuration,


            @Positive(message = "La cantidad debe ser positiva")
            Integer quantity,
            String frequency,

            Boolean requiresSpecialist,

            @Positive(message = "El ID del especialista debe ser positivo")
            Long specialistId
    ) {
    }
}
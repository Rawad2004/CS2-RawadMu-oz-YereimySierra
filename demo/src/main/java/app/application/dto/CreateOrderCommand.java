// File: src/main/java/app/application/port/in/CreateOrderCommand.java
package app.application.dto;

import app.application.usecases.OrderItemType;
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

            // Campos específicos para MEDICATION
            String dose,
            String treatmentDuration,

            // Campos específicos para PROCEDURE y DIAGNOSTIC_AID
            @Positive(message = "La cantidad debe ser positiva")
            Integer quantity,
            String frequency,

            Boolean requiresSpecialist,

            @Positive(message = "El ID del especialista debe ser positivo")
            Long specialistId
    ) {
        public OrderItemData {
            if (itemNumber == null || itemNumber <= 0) {
                throw new IllegalArgumentException("El número de ítem debe ser positivo");
            }
            if (type == null) {
                throw new IllegalArgumentException("El tipo de ítem es obligatorio");
            }
            if (itemId == null || itemId <= 0) {
                throw new IllegalArgumentException("El ID del item es obligatorio y debe ser positivo");
            }

            // Validaciones específicas por tipo
            if (type == OrderItemType.MEDICATION) {
                if (dose == null || dose.trim().isEmpty()) {
                    throw new IllegalArgumentException("La dosis es obligatoria para medicamentos");
                }
                if (treatmentDuration == null || treatmentDuration.trim().isEmpty()) {
                    throw new IllegalArgumentException("La duración del tratamiento es obligatoria para medicamentos");
                }
            } else if (type == OrderItemType.PROCEDURE || type == OrderItemType.DIAGNOSTIC_AID) {
                if (quantity == null || quantity <= 0) {
                    throw new IllegalArgumentException("La cantidad es obligatoria y debe ser positiva");
                }
            }

            if (requiresSpecialist != null && requiresSpecialist && specialistId == null) {
                throw new IllegalArgumentException("Se requiere un especialista pero no se proporcionó el ID");
            }
        }
    }

    public CreateOrderCommand {
        if (orderNumber == null || orderNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de orden es obligatorio");
        }
        if (patientNationalId == null || patientNationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        if (doctorNationalId == null || doctorNationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del médico es obligatoria");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("La orden debe contener al menos un item");
        }

        // Validar que los números de ítem sean únicos
        boolean hasDuplicateItems = items.stream()
                .map(OrderItemData::itemNumber)
                .distinct()
                .count() != items.size();

        if (hasDuplicateItems) {
            throw new IllegalArgumentException("Los números de ítem deben ser únicos dentro de la orden");
        }
    }
}
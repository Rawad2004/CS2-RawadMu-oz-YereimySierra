package app.application.port.in;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddNursingRecordCommand(
        @NotBlank(message = "El número de orden es obligatorio")
        String orderNumber,

        @NotNull(message = "El número de ítem es obligatorio")
        @Min(value = 1, message = "El número de ítem debe ser mayor o igual a 1")
        Integer itemNumber,

        @NotBlank(message = "La descripción de la intervención es obligatoria")
        String description
) {}

package app.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecordVitalSignsCommand(
        @NotNull(message = "La presión arterial es obligatoria")
        @Positive(message = "La presión arterial debe ser positiva")
        Double bloodPressure,

        @NotNull(message = "La temperatura es obligatoria")
        @Positive(message = "La temperatura debe ser positiva")
        Double temperature,

        @NotNull(message = "El pulso es obligatorio")
        @Positive(message = "El pulso debe ser positivo")
        Integer pulse,

        @NotNull(message = "El nivel de oxígeno es obligatorio")
        @Positive(message = "El nivel de oxígeno debe ser positivo")
        Double oxygenLevel
) { }

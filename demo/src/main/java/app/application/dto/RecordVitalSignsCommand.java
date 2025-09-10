// File: src/main/java/app/application/dto/RecordVitalSignsCommand.java
package app.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecordVitalSignsCommand(
        @NotNull(message = "La presión arterial sistólica es obligatoria")
        @Positive(message = "La presión arterial sistólica debe ser positiva")
        Double systolicBloodPressure,

        @NotNull(message = "La presión arterial diastólica es obligatoria")
        @Positive(message = "La presión arterial diastólica debe ser positiva")
        Double diastolicBloodPressure,

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
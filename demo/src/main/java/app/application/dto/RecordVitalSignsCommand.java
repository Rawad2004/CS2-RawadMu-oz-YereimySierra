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
) {
    public RecordVitalSignsCommand {
        if (systolicBloodPressure < diastolicBloodPressure) {
            throw new IllegalArgumentException("La presión sistólica debe ser mayor que la diastólica");
        }
        if (temperature < 25.0 || temperature > 45.0) {
            throw new IllegalArgumentException("La temperatura debe estar entre 25°C y 45°C");
        }
        if (pulse <= 0 || pulse > 250) {
            throw new IllegalArgumentException("El pulso debe estar entre 1 y 250 lpm");
        }
        if (oxygenLevel < 0.0 || oxygenLevel > 100.0) {
            throw new IllegalArgumentException("El nivel de oxígeno debe estar entre 0% y 100%");
        }
    }
}
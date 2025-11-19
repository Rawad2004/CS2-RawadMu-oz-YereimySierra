
package app.application.port.in;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record UpdateSpecialistCommand(
        @NotNull(message = "El ID del especialista es obligatorio")
        Long specialistId,

        @NotBlank(message = "El nombre de la especialidad es obligatorio")
        String specialtyName
) { }

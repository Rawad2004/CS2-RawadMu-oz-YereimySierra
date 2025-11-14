// File: src/main/java/app/application/port/in/CreateSpecialistCommand.java
package app.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSpecialistCommand(
        @NotBlank(message = "El nombre de la especialidad es obligatorio")
        @Size(max = 100, message = "El nombre de la especialidad no puede exceder 100 caracteres")
        String specialtyName
) {}

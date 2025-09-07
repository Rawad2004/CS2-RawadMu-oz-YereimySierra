package app.application.dto; // O donde lo tengas, ej: app.application.port.in

import app.domain.model.vo.StaffRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

// Dejamos solo las anotaciones. El constructor manual se elimina.
public record CreateStaffCommand(
        @NotBlank(message = "La cédula es obligatoria")
        String nationalId,

        @NotBlank(message = "El nombre completo es obligatorio")
        String fullName,

        @NotBlank(message = "El correo electrónico es obligatorio")
        String email,

        @NotBlank(message = "El número de teléfono es obligatorio")
        String phoneNumber,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser en el pasado")
        LocalDate birthDate,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 30, message = "La dirección no puede exceder 30 caracteres")
        String address,

        @NotNull(message = "El rol es obligatorio")
        StaffRole role,

        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(max = 15, message = "El nombre de usuario no puede exceder 15 caracteres")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {}
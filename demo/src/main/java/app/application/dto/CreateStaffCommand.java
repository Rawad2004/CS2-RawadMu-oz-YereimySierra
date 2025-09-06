// File: src/main/java/app/application/port/in/CreateStaffCommand.java
package app.application.dto;

import app.domain.model.vo.StaffRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

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
) {
    public CreateStaffCommand {
        // Validación de cédula
        if (nationalId == null || nationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula es obligatoria");
        }
        if (!nationalId.matches("\\d{8,10}")) {
            throw new IllegalArgumentException("El formato de la cédula no es válido");
        }

        // Validación de nombre completo
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre completo es obligatorio");
        }

        // Validación de email
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio");
        }
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("El formato del correo electrónico no es válido");
        }

        // Validación de teléfono
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de teléfono es obligatorio");
        }
        if (!phoneNumber.matches("\\d{1,10}")) {
            throw new IllegalArgumentException("El número de teléfono debe contener entre 1 y 10 dígitos");
        }

        // Validación de fecha de nacimiento
        if (birthDate == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");
        }
        if (birthDate.isAfter(LocalDate.now().minusYears(150))) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser mayor a 150 años");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser en el futuro");
        }

        // Validación de dirección
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección es obligatoria");
        }
        if (address.length() > 30) {
            throw new IllegalArgumentException("La dirección no puede exceder 30 caracteres");
        }

        // Validación de rol
        if (role == null) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }

        // Validación de nombre de usuario
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }
        if (username.length() > 15) {
            throw new IllegalArgumentException("El nombre de usuario no puede exceder 15 caracteres");
        }
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("El nombre de usuario solo debe contener letras y números");
        }

        // Validación de contraseña
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe contener por lo menos 8 caracteres");
        }
        if (!password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$")) {
            throw new IllegalArgumentException("La contraseña debe incluir una mayúscula, un número y un carácter especial");
        }
    }
}
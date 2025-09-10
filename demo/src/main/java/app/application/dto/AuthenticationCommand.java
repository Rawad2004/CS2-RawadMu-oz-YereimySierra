// File: src/main/java/app/application/port/in/AuthenticationCommand.java
package app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationCommand(
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(max = 15, message = "El nombre de usuario no puede exceder 15 caracteres")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
    // Métodos de conveniencia
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
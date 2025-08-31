// File: src/main/java/app/application/port/in/AuthenticationCommand.java
package app.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationCommand(
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(max = 15, message = "El nombre de usuario no puede exceder 15 caracteres")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
    public AuthenticationCommand {
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
        // Nota: No validamos complejidad aquí porque es el hash lo que se verifica
    }

    // Métodos de conveniencia
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
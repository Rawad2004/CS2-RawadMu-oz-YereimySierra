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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
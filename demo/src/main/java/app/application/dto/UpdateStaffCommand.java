// File: src/main/java/app/application/dto/UpdateStaffCommand.java
package app.application.dto;

import app.domain.model.vo.StaffRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateStaffCommand(
        @NotBlank(message = "La cédula del staff es obligatoria")
        String nationalId,

        String email,
        String phoneNumber,
        String address,

        StaffRole role,

        String password
) {
}
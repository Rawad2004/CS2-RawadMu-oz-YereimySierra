package app.application.dto;

import app.domain.model.vo.StaffRole;
import jakarta.validation.constraints.NotBlank;

public record UpdateStaffCommand(
        @NotBlank(message = "La cédula del staff es obligatoria")
        String nationalId,

        String email,
        String phoneNumber,
        String address,
        StaffRole role,
        String password
) {}
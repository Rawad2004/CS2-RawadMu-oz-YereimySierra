// File: src/main/java/app/application/port/in/CreatePatientCommand.java
package app.application.port.in;

import app.domain.model.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreatePatientCommand(
        @NotBlank(message = "La cédula del paciente es obligatoria")
        String nationalId,

        @NotBlank(message = "El nombre completo es obligatorio")
        String fullName,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser en el pasado")
        LocalDate birthDate,

        @NotNull(message = "El género es obligatorio")
        Gender gender,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 30, message = "La dirección no puede exceder 30 caracteres")
        String address,

        @NotBlank(message = "El número de teléfono es obligatorio")
        String phoneNumber,

        String email,

        @NotNull(message = "El contacto de emergencia es obligatorio")
        EmergencyContactData emergencyContact,

        @NotNull(message = "La información del seguro médico es obligatoria")
        InsurancePolicyData insurancePolicy
) {
    public record EmergencyContactData(
            @NotBlank(message = "El nombre del contacto de emergencia es obligatorio")
            String fullName,

            @NotBlank(message = "La relación con el paciente es obligatoria")
            String relationship,

            @NotBlank(message = "El teléfono de emergencia es obligatorio")
            String phoneNumber
    ) {

    }

    public record InsurancePolicyData(
            @NotBlank(message = "El nombre de la compañía de seguros es obligatorio")
            String companyName,

            @NotBlank(message = "El número de póliza es obligatorio")
            String policyNumber,

            boolean active,

            @NotNull(message = "La fecha de vigencia de la póliza es obligatoria")
            LocalDate expiryDate
    ) {

        }

}
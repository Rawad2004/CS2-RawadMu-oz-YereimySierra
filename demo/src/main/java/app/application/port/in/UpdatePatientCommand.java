// File: src/main/java/app/application/port/in/UpdatePatientCommand.java
package app.application.port.in;

import app.domain.model.vo.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UpdatePatientCommand(
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
        public EmergencyContactData {
            if (fullName == null || fullName.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del contacto de emergencia es obligatorio");
            }
            if (relationship == null || relationship.trim().isEmpty()) {
                throw new IllegalArgumentException("La relación con el paciente es obligatoria");
            }
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("El teléfono de emergencia es obligatorio");
            }
        }
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
        public InsurancePolicyData {
            if (companyName == null || companyName.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre de la compañía de seguros es obligatorio");
            }
            if (policyNumber == null || policyNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("El número de póliza es obligatorio");
            }
            if (expiryDate == null) {
                throw new IllegalArgumentException("La fecha de vigencia de la póliza es obligatoria");
            }
        }
    }

    public UpdatePatientCommand {
        // Validaciones del paciente
        if (nationalId == null || nationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre completo es obligatorio");
        }
        if (birthDate == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");
        }
        if (birthDate.isAfter(LocalDate.now().minusYears(150))) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser mayor a 150 años");
        }
        if (gender == null) {
            throw new IllegalArgumentException("El género es obligatorio");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección es obligatoria");
        }
        if (address.length() > 30) {
            throw new IllegalArgumentException("La dirección no puede exceder 30 caracteres");
        }
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de teléfono es obligatorio");
        }
        if (emergencyContact == null) {
            throw new IllegalArgumentException("El contacto de emergencia es obligatorio");
        }
        if (insurancePolicy == null) {
            throw new IllegalArgumentException("La información del seguro médico es obligatoria");
        }
    }
}
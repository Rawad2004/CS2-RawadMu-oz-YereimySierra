package app.infrastructure.web.dto;

import app.domain.model.enums.Gender;

import java.time.LocalDate;

public record PatientResponseDto(
        Long id,
        String nationalId,
        String fullName,
        LocalDate birthDate,
        Gender gender,
        String address,
        String phoneNumber,
        String email,
        EmergencyContactDto emergencyContact,
        InsurancePolicyDto insurancePolicy
) {
    public record EmergencyContactDto(
            String fullName,
            String relationship,
            String phoneNumber
    ) {}

    public record InsurancePolicyDto(
            String companyName,
            String policyNumber,
            boolean active,
            LocalDate expiryDate
    ) {}
}

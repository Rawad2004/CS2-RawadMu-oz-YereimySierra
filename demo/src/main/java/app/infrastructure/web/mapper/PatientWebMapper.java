package app.infrastructure.web.mapper;

import app.domain.model.Patient;
import app.infrastructure.web.dto.PatientResponseDto;

import java.util.List;

public class PatientWebMapper {

    private PatientWebMapper() {}

    public static PatientResponseDto toDto(Patient patient) {
        if (patient == null) return null;

        return new PatientResponseDto(
                patient.getId(),


                patient.getNationalId() != null
                        ? patient.getNationalId().getValue()
                        : null,

                patient.getFullName(),


                patient.getDateOfBirth() != null
                        ? patient.getDateOfBirth().getValue()
                        : null,

                patient.getGender(),


                patient.getAddress() != null
                        ? patient.getAddress().getValue()
                        : null,

                patient.getPhoneNumber() != null
                        ? patient.getPhoneNumber().getValue()
                        : null,


                patient.getEmail() != null
                        ? patient.getEmail().getValue()
                        : null,


                patient.getEmergencyContact() != null
                        ? new PatientResponseDto.EmergencyContactDto(
                        patient.getEmergencyContact().getFullName(),
                        patient.getEmergencyContact().getRelationship(),
                        patient.getEmergencyContact().getPhoneNumber() != null
                                ? patient.getEmergencyContact().getPhoneNumber().getValue()
                                : null
                )
                        : null,

                patient.getInsurancePolicy() != null
                        ? new PatientResponseDto.InsurancePolicyDto(
                        patient.getInsurancePolicy().getCompanyName(),
                        patient.getInsurancePolicy().getPolicyNumber(),
                        patient.getInsurancePolicy().isActive(),
                        patient.getInsurancePolicy().getExpiryDate()
                )
                        : null
        );
    }

    public static List<PatientResponseDto> toDtoList(List<Patient> patients) {
        return patients.stream()
                .map(PatientWebMapper::toDto)
                .toList();
    }
}

package app.application.services;

import app.domain.model.Patient;
import app.domain.model.vo.InsurancePolicy;
import app.domain.model.vo.NationalId; // ← IMPORTAR NationalId
import app.domain.repository.PatientRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class PatientInsuranceService {

    private final PatientRepositoryPort patientRepository;

    public PatientInsuranceService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient updateInsurancePolicy(String patientNationalId, String insuranceCompany,
                                         String policyNumber, LocalDate policyEndDate, boolean isActive) {

        NationalId nationalId = new NationalId(patientNationalId);

        Patient patient = patientRepository.findByNationalId(nationalId) // ← USAR NationalId
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        InsurancePolicy newPolicy = new InsurancePolicy(insuranceCompany, policyNumber, isActive, policyEndDate);

        updatePatientInsurance(patient, newPolicy);

        return patientRepository.save(patient);
    }

    public boolean isInsuranceActive(String patientNationalId) {

        NationalId nationalId = new NationalId(patientNationalId);

        Patient patient = patientRepository.findByNationalId(nationalId) // ← USAR NationalId
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        return patient.getInsurancePolicy() != null && patient.getInsurancePolicy().isActive();
    }

    private void updatePatientInsurance(Patient patient, InsurancePolicy newPolicy) {

        try {

            patient.getClass().getMethod("updateInsurancePolicy", InsurancePolicy.class)
                    .invoke(patient, newPolicy);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar la póliza del paciente", e);
        }
    }
}

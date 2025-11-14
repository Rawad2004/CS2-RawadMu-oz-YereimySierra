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
        // CONVERTIR String a NationalId
        NationalId nationalId = new NationalId(patientNationalId);

        Patient patient = patientRepository.findByNationalId(nationalId) // ← USAR NationalId
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        // Crear nueva póliza de seguro
        InsurancePolicy newPolicy = new InsurancePolicy(insuranceCompany, policyNumber, isActive, policyEndDate);

        // Actualizar la póliza del paciente (necesitamos implementar este método)
        updatePatientInsurance(patient, newPolicy);

        return patientRepository.save(patient);
    }

    public boolean isInsuranceActive(String patientNationalId) {
        // CONVERTIR String a NationalId
        NationalId nationalId = new NationalId(patientNationalId);

        Patient patient = patientRepository.findByNationalId(nationalId) // ← USAR NationalId
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        // Verificar si tiene póliza activa
        return patient.getInsurancePolicy() != null && patient.getInsurancePolicy().isActive();
    }

    // Método auxiliar para actualizar el seguro del paciente
    private void updatePatientInsurance(Patient patient, InsurancePolicy newPolicy) {
        // Necesitamos implementar este método en la entidad Patient
        // Por ahora, usamos reflexión o agregamos el método a Patient
        try {
            // Solución temporal - deberíamos agregar el método a Patient
            patient.getClass().getMethod("updateInsurancePolicy", InsurancePolicy.class)
                    .invoke(patient, newPolicy);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar la póliza del paciente", e);
        }
    }
}

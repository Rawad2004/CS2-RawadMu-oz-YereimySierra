package app.application.services;

import app.application.usecases.AdministrativeUseCases;
import app.domain.model.vo.NationalId;
import app.domain.repository.PatientRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeletePatientService implements AdministrativeUseCases.DeletePatientUseCase {

    private final PatientRepositoryPort patientRepository;

    public DeletePatientService(PatientRepositoryPort patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public void deletePatient(Long patientId) {
        if (patientId == null || patientId <= 0) {
            throw new IllegalArgumentException("ID de paciente inválido");
        }


        patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado con ID: " + patientId));


        if (hasPatientMedicalHistory(patientId)) {
            throw new IllegalStateException(
                    "No se puede eliminar el paciente porque tiene historial médico asociado. " +
                            "Considere archivar el paciente en lugar de eliminarlo."
            );
        }

        patientRepository.deleteById(patientId);
    }

    @Override
    public void deletePatientByNationalId(String nationalId) {
        if (nationalId == null || nationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía");
        }

        NationalId patientNationalId = new NationalId(nationalId);
        patientRepository.findByNationalId(patientNationalId)
                .ifPresent(patient -> deletePatient(patient.getId()));
    }

    private boolean hasPatientMedicalHistory(Long patientId) {
        return false;
    }
}

package app.application.usecase;

import app.application.port.in.CreateClinicalHistoryEntryCommand;
import app.application.port.in.CreateClinicalHistoryEntryUseCase;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.vo.NationalId;
import app.domain.model.vo.StaffRole;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import app.domain.repository.PatientRepositoryPort;
import app.domain.repository.StaffRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateClinicalHistoryEntryService implements CreateClinicalHistoryEntryUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;
    private final PatientRepositoryPort patientRepository;
    private final StaffRepositoryPort staffRepository;

    public CreateClinicalHistoryEntryService(ClinicalHistoryRepositoryPort clinicalHistoryRepository,
                                             PatientRepositoryPort patientRepository,
                                             StaffRepositoryPort staffRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
        this.patientRepository = patientRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public ClinicalHistoryEntry createClinicalHistoryEntry(CreateClinicalHistoryEntryCommand command) {
        NationalId patientId = new NationalId(command.patientNationalId());
        if (patientRepository.findByNationalId(patientId).isEmpty()) {
            throw new ResourceNotFoundException("Patient not found with the provided national ID.");
        }

        NationalId doctorId = new NationalId(command.doctorNationalId());
        staffRepository.findByNationalId(doctorId)
                .filter(staff -> staff.getRole() == StaffRole.DOCTOR)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with the provided national ID."));

        if (clinicalHistoryRepository.findByPatientIdAndVisitDate(patientId, command.visitDate()).isPresent()) {
            throw new IllegalStateException("Clinical history entry already exists for this patient on the specified date.");
        }

        ClinicalHistoryEntry newEntry = new ClinicalHistoryEntry(
                patientId,
                doctorId,
                command.visitDate(),
                command.reasonForVisit(),
                command.symptomatology(),
                command.diagnosis()
        );

        return clinicalHistoryRepository.save(newEntry);
    }
}
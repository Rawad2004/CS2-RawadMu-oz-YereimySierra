// File: src/main/java/app/application/services/CreateClinicalHistoryEntryService.java
package app.application.services;

import app.application.port.in.CreateClinicalHistoryEntryCommand;
import app.application.usecases.DoctorUseCases.CreateClinicalHistoryEntryUseCase;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.vo.NationalId;
import app.domain.model.enums.StaffRole;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import app.domain.repository.PatientRepositoryPort;
import app.domain.repository.StaffRepositoryPort;
import org.springframework.stereotype.Service;

@Service
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
        // Validar que el paciente existe
        NationalId patientId = new NationalId(command.patientNationalId());
        if (patientRepository.findByNationalId(patientId).isEmpty()) {
            throw new ResourceNotFoundException("Paciente no encontrado con cédula: " + command.patientNationalId());
        }

        // Validar que el doctor existe y es médico
        NationalId doctorId = new NationalId(command.doctorNationalId());
        staffRepository.findByNationalId(doctorId)
                .filter(staff -> staff.getRole() == StaffRole.DOCTOR)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con cédula: " + command.doctorNationalId()));

        // Validar que no existe visita para la misma fecha
        if (clinicalHistoryRepository.existsVisit(command.patientNationalId(), command.visitDate())) {
            throw new IllegalStateException("Ya existe una visita para este paciente en la fecha: " + command.visitDate());
        }

        // Determinar el tipo de orden
        ClinicalHistoryEntry.OrderType orderType = determineOrderType(command);

        // Crear la nueva visita en la historia clínica
        return clinicalHistoryRepository.addVisit(
                command.patientNationalId(),
                command.visitDate(),
                command.doctorNationalId(),
                command.reasonForVisit(),
                command.symptomatology(),
                command.diagnosis(),
                command.associatedOrderNumber(),
                orderType
        );
    }

    private ClinicalHistoryEntry.OrderType determineOrderType(CreateClinicalHistoryEntryCommand command) {
        if (command.visitType() != null) {
            return command.visitType();
        }

        if (command.associatedOrderNumber() != null && !command.associatedOrderNumber().trim().isEmpty()) {
            return ClinicalHistoryEntry.OrderType.DIAGNOSTIC_AID; // Valor por defecto si hay orden
        }

        return ClinicalHistoryEntry.OrderType.NONE;
    }
}
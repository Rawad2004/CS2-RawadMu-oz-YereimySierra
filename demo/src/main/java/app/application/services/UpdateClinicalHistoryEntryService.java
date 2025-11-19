package app.application.services;

import app.application.port.in.UpdateClinicalHistoryEntryCommand;
import app.application.usecases.DoctorUseCases;
import app.application.usecases.DoctorUseCases.UpdateClinicalHistoryEntryUseCase;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class UpdateClinicalHistoryEntryService implements DoctorUseCases.UpdateClinicalHistoryEntryUseCase{

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public UpdateClinicalHistoryEntryService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public ClinicalHistoryEntry updateClinicalHistoryEntry(UpdateClinicalHistoryEntryCommand command) {

        ClinicalHistoryEntry entry = clinicalHistoryRepository.findByPatientNationalId(command.patientNationalId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró historia clínica para el paciente: " + command.patientNationalId()));


        if (!entry.hasVisitOnDate(command.originalVisitDate())) {
            throw new IllegalArgumentException(
                    "No existe visita para el paciente " + command.patientNationalId() +
                            " en la fecha: " + command.originalVisitDate());
        }


        ClinicalHistoryEntry.VisitData existingVisit = entry.getVisit(command.originalVisitDate());


        if (existingVisit.getDiagnosis() != null &&
                existingVisit.getDiagnosis().equals(command.newDiagnosis()) &&
                (command.updateNotes() == null || command.updateNotes().trim().isEmpty())) {
            throw new IllegalStateException("No hay cambios para actualizar en la entrada de historia clínica");
        }


        return clinicalHistoryRepository.updateDiagnosis(
                command.patientNationalId(),
                command.originalVisitDate(),
                command.newDiagnosis(),
                command.updateNotes()
        );
    }


    public ClinicalHistoryEntry updateDiagnosisOnly(String patientNationalId, LocalDate visitDate, String newDiagnosis) {
        ClinicalHistoryEntry entry = clinicalHistoryRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró historia clínica para el paciente: " + patientNationalId));

        if (!entry.hasVisitOnDate(visitDate)) {
            throw new IllegalArgumentException(
                    "No existe visita para el paciente " + patientNationalId + " en la fecha: " + visitDate);
        }

        return clinicalHistoryRepository.updateDiagnosis(
                patientNationalId,
                visitDate,
                newDiagnosis,
                null
        );
    }


    public ClinicalHistoryEntry updateDiagnosisAfterDiagnosticAid(String patientNationalId, LocalDate diagnosticVisitDate,
                                                                  String diagnosis, String diagnosticResults) {
        ClinicalHistoryEntry entry = clinicalHistoryRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró historia clínica para el paciente: " + patientNationalId));


        ClinicalHistoryEntry.VisitData visit = entry.getVisit(diagnosticVisitDate);
        if (visit == null) {
            throw new IllegalArgumentException("No existe visita en la fecha: " + diagnosticVisitDate);
        }

        if (visit.getOrderType() != ClinicalHistoryEntry.OrderType.DIAGNOSTIC_AID) {
            throw new IllegalStateException("La visita no es de tipo ayuda diagnóstica");
        }

        String updateNotes = "Resultados de ayuda diagnóstica: " + diagnosticResults;
        return clinicalHistoryRepository.updateDiagnosis(
                patientNationalId,
                diagnosticVisitDate,
                diagnosis,
                updateNotes
        );
    }


    public ClinicalHistoryEntry addVisitNotes(String patientNationalId, LocalDate visitDate, String additionalNotes) {
        ClinicalHistoryEntry entry = clinicalHistoryRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró historia clínica para el paciente: " + patientNationalId));

        if (!entry.hasVisitOnDate(visitDate)) {
            throw new IllegalArgumentException(
                    "No existe visita para el paciente " + patientNationalId + " en la fecha: " + visitDate);
        }

        ClinicalHistoryEntry.VisitData visit = entry.getVisit(visitDate);
        String currentDiagnosis = visit.getDiagnosis();

        return clinicalHistoryRepository.updateDiagnosis(
                patientNationalId,
                visitDate,
                currentDiagnosis,
                additionalNotes
        );
    }


    public ClinicalHistoryEntry completeVisit(String patientNationalId, LocalDate visitDate, String completionNotes) {
        ClinicalHistoryEntry entry = clinicalHistoryRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró historia clínica para el paciente: " + patientNationalId));

        if (!entry.hasVisitOnDate(visitDate)) {
            throw new IllegalArgumentException(
                    "No existe visita para el paciente " + patientNationalId + " en la fecha: " + visitDate);
        }

        ClinicalHistoryEntry.VisitData visit = entry.getVisit(visitDate);

        if (visit.getOrderStatus() == ClinicalHistoryEntry.OrderStatus.NO_ORDER) {
            throw new IllegalStateException("No se puede completar una visita sin orden asociada");
        }

        visit.markAsCompleted();

        if (completionNotes != null && !completionNotes.trim().isEmpty()) {
            String currentNotes = visit.getUpdateNotes() != null ? visit.getUpdateNotes() + "\n" : "";
            visit.setUpdateNotes(currentNotes + "Completado: " + completionNotes);
        }

        return clinicalHistoryRepository.save(entry);
    }
}
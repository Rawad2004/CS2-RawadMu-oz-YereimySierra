// File: src/main/java/app/application/services/UpdateClinicalHistoryEntryService.java
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
public class UpdateClinicalHistoryEntryService implements DoctorUseCases.UpdateClinicalHistoryEntryUseCase,
        DoctorUseCases.UpdateDiagnosisOnlyUseCase,
        DoctorUseCases.UpdateDiagnosisAfterDiagnosticAidUseCase,
        DoctorUseCases.AddVisitNotesUseCase,
        DoctorUseCases.CompleteVisitUseCase  {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public UpdateClinicalHistoryEntryService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public ClinicalHistoryEntry updateClinicalHistoryEntry(UpdateClinicalHistoryEntryCommand command) {
        // 1. Buscar la entrada de historia clínica del paciente
        ClinicalHistoryEntry entry = clinicalHistoryRepository.findByPatientNationalId(command.patientNationalId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró historia clínica para el paciente: " + command.patientNationalId()));

        // 2. Verificar que existe una visita en la fecha original
        if (!entry.hasVisitOnDate(command.originalVisitDate())) {
            throw new IllegalArgumentException(
                    "No existe visita para el paciente " + command.patientNationalId() +
                            " en la fecha: " + command.originalVisitDate());
        }

        // 3. Obtener la visita existente para validar cambios
        ClinicalHistoryEntry.VisitData existingVisit = entry.getVisit(command.originalVisitDate());

        // 4. Validar que hay cambios reales para actualizar
        if (existingVisit.getDiagnosis() != null &&
                existingVisit.getDiagnosis().equals(command.newDiagnosis()) &&
                (command.updateNotes() == null || command.updateNotes().trim().isEmpty())) {
            throw new IllegalStateException("No hay cambios para actualizar en la entrada de historia clínica");
        }

        // 5. Actualizar el diagnóstico y notas
        return clinicalHistoryRepository.updateDiagnosis(
                command.patientNationalId(),
                command.originalVisitDate(),
                command.newDiagnosis(),
                command.updateNotes()
        );
    }

    /**
     * Método específico para actualizar solo el diagnóstico sin notas adicionales
     */
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
                null // Sin notas adicionales
        );
    }

    /**
     * Método para actualizar el diagnóstico después de una ayuda diagnóstica
     * (Completa el flujo: ayuda diagnóstica → diagnóstico)
     */
    public ClinicalHistoryEntry updateDiagnosisAfterDiagnosticAid(String patientNationalId, LocalDate diagnosticVisitDate,
                                                                  String diagnosis, String diagnosticResults) {
        ClinicalHistoryEntry entry = clinicalHistoryRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró historia clínica para el paciente: " + patientNationalId));

        // Verificar que la visita existe y es de tipo ayuda diagnóstica
        ClinicalHistoryEntry.VisitData visit = entry.getVisit(diagnosticVisitDate);
        if (visit == null) {
            throw new IllegalArgumentException("No existe visita en la fecha: " + diagnosticVisitDate);
        }

        if (visit.getOrderType() != ClinicalHistoryEntry.OrderType.DIAGNOSTIC_AID) {
            throw new IllegalStateException("La visita no es de tipo ayuda diagnóstica");
        }

        // Actualizar el diagnóstico con los resultados
        String updateNotes = "Resultados de ayuda diagnóstica: " + diagnosticResults;
        return clinicalHistoryRepository.updateDiagnosis(
                patientNationalId,
                diagnosticVisitDate,
                diagnosis,
                updateNotes
        );
    }

    /**
     * Método para agregar notas adicionales a una visita existente
     */
    public ClinicalHistoryEntry addVisitNotes(String patientNationalId, LocalDate visitDate, String additionalNotes) {
        ClinicalHistoryEntry entry = clinicalHistoryRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró historia clínica para el paciente: " + patientNationalId));

        if (!entry.hasVisitOnDate(visitDate)) {
            throw new IllegalArgumentException(
                    "No existe visita para el paciente " + patientNationalId + " en la fecha: " + visitDate);
        }

        // Obtener el diagnóstico actual para no cambiarlo
        ClinicalHistoryEntry.VisitData visit = entry.getVisit(visitDate);
        String currentDiagnosis = visit.getDiagnosis();

        return clinicalHistoryRepository.updateDiagnosis(
                patientNationalId,
                visitDate,
                currentDiagnosis, // Mantener el mismo diagnóstico
                additionalNotes   // Agregar las nuevas notas
        );
    }

    /**
     * Método para completar una visita (marcar como completada)
     */
    public ClinicalHistoryEntry completeVisit(String patientNationalId, LocalDate visitDate, String completionNotes) {
        ClinicalHistoryEntry entry = clinicalHistoryRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró historia clínica para el paciente: " + patientNationalId));

        if (!entry.hasVisitOnDate(visitDate)) {
            throw new IllegalArgumentException(
                    "No existe visita para el paciente " + patientNationalId + " en la fecha: " + visitDate);
        }

        ClinicalHistoryEntry.VisitData visit = entry.getVisit(visitDate);

        // Solo se puede completar si tiene una orden asociada
        if (visit.getOrderStatus() == ClinicalHistoryEntry.OrderStatus.NO_ORDER) {
            throw new IllegalStateException("No se puede completar una visita sin orden asociada");
        }

        // Marcar como completada
        visit.markAsCompleted();

        // Agregar notas de completación si se proporcionan
        if (completionNotes != null && !completionNotes.trim().isEmpty()) {
            String currentNotes = visit.getUpdateNotes() != null ? visit.getUpdateNotes() + "\n" : "";
            visit.setUpdateNotes(currentNotes + "Completado: " + completionNotes);
        }

        return clinicalHistoryRepository.save(entry);
    }
}
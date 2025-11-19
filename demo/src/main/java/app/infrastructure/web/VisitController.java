package app.infrastructure.web;

import app.application.port.in.*;
import app.application.usecases.DoctorUseCases;
import app.application.usecases.NurseUseCases;
import app.domain.exception.ResourceNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.application.port.in.UpdateDiagnosisCommand;
import org.springframework.beans.factory.annotation.Qualifier;


import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/patients/{nationalId}/visits")
public class VisitController {

    private final DoctorUseCases.DeleteVisitUseCase deleteVisitUseCase;
    private final DoctorUseCases.RegisterVisitUseCase registerVisitUseCase;
    private final DoctorUseCases.GetPatientVisitsUseCase getPatientVisitsUseCase;
    private final NurseUseCases.RecordVitalSignsUseCase recordVitalSignsUseCase;
    private final NurseUseCases.RecordNursingInterventionUseCase recordNursingInterventionUseCase;
    private final DoctorUseCases.GetVisitDetailUseCase getVisitDetailUseCase;
    private final DoctorUseCases.UpdateVisitDiagnosisUseCase updateVisitDiagnosisUseCase;
    private final DoctorUseCases.AssociateOrderToVisitUseCase associateOrderToVisitUseCase;
    private final DoctorUseCases.MarkVisitPendingDiagnosticUseCase markVisitPendingDiagnosticUseCase;

    private final DoctorUseCases.AddVisitNotesUseCase addVisitNotesUseCase;

    private final DoctorUseCases.CompleteVisitUseCase completeVisitUseCase;

    public VisitController(DoctorUseCases.DeleteVisitUseCase deleteVisitUseCase,
                           DoctorUseCases.RegisterVisitUseCase registerVisitUseCase,
                           DoctorUseCases.GetPatientVisitsUseCase getPatientVisitsUseCase,
                           NurseUseCases.RecordVitalSignsUseCase recordVitalSignsUseCase,
                           NurseUseCases.RecordNursingInterventionUseCase recordNursingInterventionUseCase,
                           DoctorUseCases.GetVisitDetailUseCase getVisitDetailUseCase,
                           DoctorUseCases.UpdateVisitDiagnosisUseCase updateVisitDiagnosisUseCase,
                           DoctorUseCases.AssociateOrderToVisitUseCase associateOrderToVisitUseCase,
                           DoctorUseCases.MarkVisitPendingDiagnosticUseCase markVisitPendingDiagnosticUseCase,
                           DoctorUseCases.AddVisitNotesUseCase addVisitNotesUseCase,
                           DoctorUseCases.CompleteVisitUseCase completeVisitUseCase
    ) {
        this.deleteVisitUseCase = deleteVisitUseCase;
        this.registerVisitUseCase = registerVisitUseCase;
        this.getPatientVisitsUseCase = getPatientVisitsUseCase;
        this.recordVitalSignsUseCase = recordVitalSignsUseCase;
        this.recordNursingInterventionUseCase = recordNursingInterventionUseCase;
        this.getVisitDetailUseCase = getVisitDetailUseCase;
        this.updateVisitDiagnosisUseCase = updateVisitDiagnosisUseCase;
        this.associateOrderToVisitUseCase = associateOrderToVisitUseCase;
        this.markVisitPendingDiagnosticUseCase = markVisitPendingDiagnosticUseCase;
        this.addVisitNotesUseCase = addVisitNotesUseCase;
        this.completeVisitUseCase = completeVisitUseCase;
    }

    @GetMapping
    public ResponseEntity<List<VisitSummaryResponse>> getVisits(
            @PathVariable String nationalId
    ) {
        List<VisitSummaryResponse> visits = getPatientVisitsUseCase.getVisits(nationalId);
        return ResponseEntity.ok(visits);
    }

    @PostMapping("/{visitDate}")
    public ResponseEntity<Void> registerVisit(
            @PathVariable String nationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
            @RequestBody RegisterVisitCommand command
    ) {
        registerVisitUseCase.registerVisit(nationalId, visitDate, command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{visitDate}")
    public ResponseEntity<Void> deleteVisit(
            @PathVariable String nationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate
    ) {
        deleteVisitUseCase.deleteVisit(nationalId, visitDate);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{visitDate}/vital-signs")
    public ResponseEntity<Void> recordVitalSigns(
            @PathVariable String nationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
            @RequestBody @Valid RecordVitalSignsCommand command
    ) {
        recordVitalSignsUseCase.recordVitalSigns(nationalId, command, visitDate);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @PostMapping("/{visitDate}/nursing-records")
    public ResponseEntity<Void> recordNursingIntervention(
            @PathVariable String nationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
            @RequestBody @jakarta.validation.Valid RecordNursingInterventionCommand command
    ) {
        recordNursingInterventionUseCase.recordNursingIntervention(nationalId, visitDate, command);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{visitDate}")
    public ResponseEntity<VisitDetailResponse> getVisitDetail(
            @PathVariable String nationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate
    ) {
        VisitDetailResponse detail = getVisitDetailUseCase.getVisitDetail(nationalId, visitDate);
        return ResponseEntity.ok(detail);
    }


    @PutMapping("/{visitDate}/diagnosis")
    public ResponseEntity<Void> updateDiagnosis(
            @PathVariable String nationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
            @RequestBody @jakarta.validation.Valid UpdateDiagnosisCommand command
    ) {
        UpdateDiagnosisCommand adjusted = new UpdateDiagnosisCommand(
                nationalId,
                visitDate,
                command.newDiagnosis(),
                command.diagnosticResults(),
                command.updateDate() != null ? command.updateDate() : LocalDate.now()
        );

        updateVisitDiagnosisUseCase.updateDiagnosis(adjusted);

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{visitDate}/order")
    public ResponseEntity<Void> associateOrderToVisit(
            @PathVariable String nationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
            @RequestBody @jakarta.validation.Valid AssociateOrderToVisitCommand command
    ) {
        associateOrderToVisitUseCase.associateOrder(nationalId, visitDate, command);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{visitDate}/pending-diagnostic")
    public ResponseEntity<Void> markVisitPendingDiagnostic(
            @PathVariable String nationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate
    ) {
        markVisitPendingDiagnosticUseCase.markPendingDiagnostic(nationalId, visitDate);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{visitDate}/notes")
    public ResponseEntity<Void> addVisitNotes(
            @PathVariable String nationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
            @RequestBody @Valid AddVisitNotesRequest request
    ) {
        addVisitNotesUseCase.addVisitNotes(
                nationalId,
                visitDate,
                request.additionalNotes
        );
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{visitDate}/complete")
    public ResponseEntity<Void> completeVisit(
            @PathVariable String nationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
            @RequestBody @Valid CompleteVisitRequest request
    ) {
        completeVisitUseCase.completeVisit(
                nationalId,
                visitDate,
                request.completionNotes
        );
        return ResponseEntity.noContent().build();
    }


    public static class AddVisitNotesRequest {

        @jakarta.validation.constraints.NotBlank(message = "Las notas adicionales son obligatorias")
        private String additionalNotes;

        public String getAdditionalNotes() {
            return additionalNotes;
        }

        public void setAdditionalNotes(String additionalNotes) {
            this.additionalNotes = additionalNotes;
        }
    }

    public static class CompleteVisitRequest {

        @jakarta.validation.constraints.NotBlank(message = "Las notas de cierre son obligatorias")
        private String completionNotes;

        public String getCompletionNotes() {
            return completionNotes;
        }

        public void setCompletionNotes(String completionNotes) {
            this.completionNotes = completionNotes;
        }
    }
}





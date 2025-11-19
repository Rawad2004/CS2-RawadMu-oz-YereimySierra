package app.infrastructure.web;

import app.application.port.in.*;
import app.application.usecases.DoctorUseCases;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/clinical-history")
public class ClinicalHistoryController {

    private final DoctorUseCases.UpdateClinicalHistoryEntryUseCase updateClinicalHistoryEntryUseCase;
    private final DoctorUseCases.UpdateDiagnosisOnlyUseCase updateDiagnosisOnlyUseCase;
    private final DoctorUseCases.UpdateDiagnosisAfterDiagnosticAidUseCase updateDiagnosisAfterDiagnosticAidUseCase;

    public ClinicalHistoryController(
            DoctorUseCases.UpdateClinicalHistoryEntryUseCase updateClinicalHistoryEntryUseCase,
            DoctorUseCases.UpdateDiagnosisOnlyUseCase updateDiagnosisOnlyUseCase,
            DoctorUseCases.UpdateDiagnosisAfterDiagnosticAidUseCase updateDiagnosisAfterDiagnosticAidUseCase
    ) {
        this.updateClinicalHistoryEntryUseCase = updateClinicalHistoryEntryUseCase;
        this.updateDiagnosisOnlyUseCase = updateDiagnosisOnlyUseCase;
        this.updateDiagnosisAfterDiagnosticAidUseCase = updateDiagnosisAfterDiagnosticAidUseCase;
    }


    @PutMapping("/update-entry")
    public ResponseEntity<Void> updateClinicalHistoryEntry(
            @RequestBody @Valid UpdateClinicalHistoryEntryCommand command
    ) {
        updateClinicalHistoryEntryUseCase.updateClinicalHistoryEntry(command);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{nationalId}/{visitDate}/diagnosis-only")
    public ResponseEntity<Void> updateDiagnosisOnly(
            @PathVariable String nationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
            @RequestBody String newDiagnosis
    ) {
        updateDiagnosisOnlyUseCase.updateDiagnosisOnly(nationalId, visitDate, newDiagnosis);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{nationalId}/{visitDate}/diagnosis-after-aid")
    public ResponseEntity<Void> updateDiagnosisAfterAid(
            @PathVariable String nationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
            @RequestBody DiagnosticAidRequest request
    ) {
        updateDiagnosisAfterDiagnosticAidUseCase.updateDiagnosisAfterDiagnosticAid(
                nationalId,
                visitDate,
                request.diagnosis(),
                request.diagnosticResults()
        );
        return ResponseEntity.noContent().build();
    }


    public record DiagnosticAidRequest(
            String diagnosis,
            String diagnosticResults
    ) {}
}

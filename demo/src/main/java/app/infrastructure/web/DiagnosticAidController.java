package app.infrastructure.web;

import app.application.port.in.CreateDiagnosticAidCommand;
import app.application.port.in.UpdateDiagnosticAidResultCommand;
import app.application.usecases.DoctorUseCases;
import app.domain.model.ClinicalHistoryEntry;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/visits/{nationalId}/{visitDate}/diagnostic-aids")
public class DiagnosticAidController {

    private final DoctorUseCases.AssociateOrderToVisitUseCase associateOrderToVisitUseCase;
    private final DoctorUseCases.UpdateDiagnosisAfterDiagnosticAidUseCase updateDiagnosisAfterDiagnosticAidUseCase;

    public DiagnosticAidController(DoctorUseCases.AssociateOrderToVisitUseCase associateOrderToVisitUseCase,
                                   DoctorUseCases.UpdateDiagnosisAfterDiagnosticAidUseCase updateDiagnosisAfterDiagnosticAidUseCase) {
        this.associateOrderToVisitUseCase = associateOrderToVisitUseCase;
        this.updateDiagnosisAfterDiagnosticAidUseCase = updateDiagnosisAfterDiagnosticAidUseCase;
    }


    @PostMapping
    public ResponseEntity<Void> createDiagnosticAid(
            @PathVariable String nationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
            @Valid @RequestBody CreateDiagnosticAidCommand command
    ) {

        associateOrderToVisitUseCase.associateOrder(
                nationalId,
                visitDate,
                command.toAssociateOrderCommand()
        );

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/results")
    public ResponseEntity<ClinicalHistoryEntry> updateDiagnosticAidResults(
            @PathVariable String nationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
            @Valid @RequestBody UpdateDiagnosticAidResultCommand command
    ) {

        ClinicalHistoryEntry updated = updateDiagnosisAfterDiagnosticAidUseCase.updateDiagnosisAfterDiagnosticAid(
                nationalId,
                visitDate,
                command.diagnosis(),
                command.results()
        );

        return ResponseEntity.ok(updated);
    }
}

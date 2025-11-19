package app.infrastructure.web;

import app.application.port.in.AddNursingRecordCommand;
import app.application.port.in.RecordMedicationAdministrationCommand;
import app.application.port.in.RecordVitalSignsCommand;
import app.application.usecases.NurseUseCases;
import app.domain.model.Patient;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/nurse")
public class NurseRestController {

    private final NurseUseCases.RecordMedicationAdministrationUseCase recordMedicationAdministrationUseCase;
    private final NurseUseCases.RecordVitalSignsUseCase recordVitalSignsUseCase;
    private final NurseUseCases.AddNursingRecordUseCase addNursingRecordUseCase;

    public NurseRestController(
            NurseUseCases.RecordMedicationAdministrationUseCase recordMedicationAdministrationUseCase,
            NurseUseCases.RecordVitalSignsUseCase recordVitalSignsUseCase,
            NurseUseCases.AddNursingRecordUseCase addNursingRecordUseCase
    ) {
        this.recordMedicationAdministrationUseCase = recordMedicationAdministrationUseCase;
        this.recordVitalSignsUseCase = recordVitalSignsUseCase;
        this.addNursingRecordUseCase = addNursingRecordUseCase;
    }

    @PreAuthorize("hasAnyRole('NURSE')")
    @PostMapping("/patients/{patientNationalId}/medications/administrations")
    public ResponseEntity<String> recordMedicationAdministration(
            @PathVariable String patientNationalId,
            @RequestBody @Valid RecordMedicationAdministrationCommand command
    ) {
        Patient patient = recordMedicationAdministrationUseCase
                .recordMedicationAdministration(patientNationalId, command);

        String msg = "Administración registrada correctamente para el paciente con cédula "
                + patient.getNationalId().getValue();
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    @PreAuthorize("hasAnyRole('NURSE')")
    @PostMapping("/patients/{patientNationalId}/visits/{visitDate}/vital-signs")
    public ResponseEntity<String> recordVitalSigns(
            @PathVariable String patientNationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
            @RequestBody @Valid RecordVitalSignsCommand command
    ) {

        recordVitalSignsUseCase.recordVitalSigns(patientNationalId, command, visitDate);

        String msg = "Signos vitales registrados para el paciente " + patientNationalId +
                " en la fecha " + visitDate;
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    @PreAuthorize("hasAnyRole('NURSE')")
    @PostMapping("/patients/{patientNationalId}/visits/{visitDate}/nursing-records")
    public ResponseEntity<String> addNursingRecord(
            @PathVariable String patientNationalId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
            @RequestBody @Valid AddNursingRecordCommand command
    ) {
        addNursingRecordUseCase.addNursingRecord(patientNationalId, visitDate, command);

        String msg = "Registro de enfermería agregado para el paciente " + patientNationalId +
                " en la fecha " + visitDate;
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }
}

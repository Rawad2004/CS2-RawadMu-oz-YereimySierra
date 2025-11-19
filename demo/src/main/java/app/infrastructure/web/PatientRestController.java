// File: src/main/java/app/infrastructure/web/PatientRestController.java
package app.infrastructure.web;

import app.application.port.in.CreatePatientCommand;
import app.application.port.in.UpdatePatientCommand;
import app.application.usecases.AdministrativeUseCases;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.Patient;
import app.infrastructure.web.dto.PatientResponseDto;
import app.infrastructure.web.mapper.PatientWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientRestController {

    private final AdministrativeUseCases.CreatePatientUseCase createPatientUseCase;
    private final AdministrativeUseCases.UpdatePatientUseCase updatePatientUseCase;
    private final AdministrativeUseCases.FindPatientUseCase findPatientUseCase;
    private final AdministrativeUseCases.DeletePatientUseCase deletePatientUseCase;

    public PatientRestController(AdministrativeUseCases.CreatePatientUseCase createPatientUseCase,
                                 AdministrativeUseCases.UpdatePatientUseCase updatePatientUseCase,
                                 AdministrativeUseCases.FindPatientUseCase findPatientUseCase,
                                 AdministrativeUseCases.DeletePatientUseCase deletePatientUseCase) {
        this.createPatientUseCase = createPatientUseCase;
        this.updatePatientUseCase = updatePatientUseCase;
        this.findPatientUseCase = findPatientUseCase;
        this.deletePatientUseCase = deletePatientUseCase;
    }


    @PreAuthorize("hasRole('ADMINISTRATIVE_STAFF')")
    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(
            @RequestBody @Valid CreatePatientCommand command
    ) {
        Patient created = createPatientUseCase.createPatient(command);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PatientWebMapper.toDto(created));
    }


    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        List<Patient> patients = findPatientUseCase.findAllPatients();
        return ResponseEntity.ok(PatientWebMapper.toDtoList(patients));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable Long id) {
        Patient patient = findPatientUseCase.findById(id);
        return ResponseEntity.ok(PatientWebMapper.toDto(patient));
    }

    @GetMapping("/national-id/{nationalId}")
    public ResponseEntity<PatientResponseDto> getPatientByNationalId(@PathVariable String nationalId) {
        Patient patient = findPatientUseCase.findByNationalId(nationalId);
        return ResponseEntity.ok(PatientWebMapper.toDto(patient));
    }


    @PreAuthorize("hasRole('ADMINISTRATIVE_STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatientById(
            @PathVariable Long id,
            @RequestBody @Valid UpdatePatientCommand command
    ) {

        UpdatePatientCommand adjusted = new UpdatePatientCommand(
                id,
                command.nationalId(),
                command.fullName(),
                command.birthDate(),
                command.gender(),
                command.address(),
                command.phoneNumber(),
                command.email(),
                command.emergencyContact(),
                command.insurancePolicy()
        );

        Patient updated = updatePatientUseCase.updatePatient(adjusted);
        return ResponseEntity.ok(PatientWebMapper.toDto(updated));
    }


    @PreAuthorize("hasRole('ADMINISTRATIVE_STAFF')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatientById(@PathVariable Long id) {
        deletePatientUseCase.deletePatient(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasRole('ADMINISTRATIVE_STAFF')")
    @DeleteMapping("/national-id/{nationalId}")
    public ResponseEntity<Void> deletePatientByNationalId(@PathVariable String nationalId) {
        deletePatientUseCase.deletePatientByNationalId(nationalId);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}

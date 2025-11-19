package app.infrastructure.web;

import app.application.port.in.CreateMedicationCommand;
import app.application.port.in.UpdateMedicationCommand;
import app.application.usecases.SupportUseCases;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.Medication;
import app.infrastructure.web.dto.MedicationResponseDto;
import app.infrastructure.web.mapper.MedicationWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
public class MedicationRestController {

    private final SupportUseCases.CreateMedicationUseCase createMedicationUseCase;
    private final SupportUseCases.FindMedicationUseCase findMedicationUseCase;
    private final SupportUseCases.UpdateMedicationUseCase updateMedicationUseCase;
    private final SupportUseCases.DeleteMedicationUseCase deleteMedicationUseCase;

    public MedicationRestController(SupportUseCases.CreateMedicationUseCase createMedicationUseCase,
                                    SupportUseCases.FindMedicationUseCase findMedicationUseCase,
                                    SupportUseCases.UpdateMedicationUseCase updateMedicationUseCase,
                                    SupportUseCases.DeleteMedicationUseCase deleteMedicationUseCase) {
        this.createMedicationUseCase = createMedicationUseCase;
        this.findMedicationUseCase = findMedicationUseCase;
        this.updateMedicationUseCase = updateMedicationUseCase;
        this.deleteMedicationUseCase = deleteMedicationUseCase;
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATIVE_STAFF','INFORMATION_SUPPORT')")
    @PostMapping
    public ResponseEntity<MedicationResponseDto> createMedication(
            @RequestBody @Valid CreateMedicationCommand command
    ) {
        Medication created = createMedicationUseCase.createMedication(command);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MedicationWebMapper.toDto(created));
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATIVE_STAFF','INFORMATION_SUPPORT','DOCTOR','NURSE')")
    @GetMapping("/{id}")
    public ResponseEntity<MedicationResponseDto> getMedicationById(@PathVariable Long id) {
        Medication medication = findMedicationUseCase.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento no encontrado con ID: " + id));

        return ResponseEntity.ok(MedicationWebMapper.toDto(medication));
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATIVE_STAFF','INFORMATION_SUPPORT','DOCTOR','NURSE')")
    @GetMapping("/by-name")
    public ResponseEntity<MedicationResponseDto> getMedicationByName(@RequestParam String name) {
        Medication medication = findMedicationUseCase.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento no encontrado con nombre: " + name));

        return ResponseEntity.ok(MedicationWebMapper.toDto(medication));
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATIVE_STAFF','INFORMATION_SUPPORT','DOCTOR','NURSE')")
    @GetMapping
    public ResponseEntity<List<MedicationResponseDto>> getAllMedications() {
        List<Medication> medications = findMedicationUseCase.findAll();
        return ResponseEntity.ok(
                medications.stream()
                        .map(MedicationWebMapper::toDto)
                        .toList()
        );
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATIVE_STAFF','INFORMATION_SUPPORT')")
    @PutMapping("/{id}")
    public ResponseEntity<MedicationResponseDto> updateMedication(
            @PathVariable Long id,
            @RequestBody @Valid UpdateMedicationCommand command
    ) {
        UpdateMedicationCommand adjusted = new UpdateMedicationCommand(
                id,
                command.name(),
                command.cost()
        );

        Medication updated = updateMedicationUseCase.updateMedication(adjusted);
        return ResponseEntity.ok(MedicationWebMapper.toDto(updated));
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATIVE_STAFF','INFORMATION_SUPPORT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        deleteMedicationUseCase.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}

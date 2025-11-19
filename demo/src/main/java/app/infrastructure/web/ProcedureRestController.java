package app.infrastructure.web;

import app.application.port.in.CreateProcedureCommand;
import app.application.port.in.UpdateProcedureCommand;
import app.application.usecases.SupportUseCases;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.Procedure;
import app.infrastructure.web.dto.ProcedureResponseDto;
import app.infrastructure.web.mapper.ProcedureWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procedures")
public class ProcedureRestController {

    private final SupportUseCases.CreateProcedureUseCase createProcedureUseCase;
    private final SupportUseCases.FindProcedureUseCase findProcedureUseCase;
    private final SupportUseCases.UpdateProcedureUseCase updateProcedureUseCase;
    private final SupportUseCases.DeleteProcedureUseCase deleteProcedureUseCase;

    public ProcedureRestController(SupportUseCases.CreateProcedureUseCase createProcedureUseCase,
                                   SupportUseCases.FindProcedureUseCase findProcedureUseCase,
                                   SupportUseCases.UpdateProcedureUseCase updateProcedureUseCase,
                                   SupportUseCases.DeleteProcedureUseCase deleteProcedureUseCase) {
        this.createProcedureUseCase = createProcedureUseCase;
        this.findProcedureUseCase = findProcedureUseCase;
        this.updateProcedureUseCase = updateProcedureUseCase;
        this.deleteProcedureUseCase = deleteProcedureUseCase;
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATIVE_STAFF','INFORMATION_SUPPORT')")
    @PostMapping
    public ResponseEntity<ProcedureResponseDto> createProcedure(
            @RequestBody @Valid CreateProcedureCommand command
    ) {
        Procedure created = createProcedureUseCase.createProcedure(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProcedureWebMapper.toDto(created));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATIVE_STAFF','INFORMATION_SUPPORT','DOCTOR','NURSE')")
    @GetMapping("/{id}")
    public ResponseEntity<ProcedureResponseDto> getProcedureById(@PathVariable Long id) {
        Procedure procedure = findProcedureUseCase.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Procedimiento no encontrado con ID: " + id
                ));
        return ResponseEntity.ok(ProcedureWebMapper.toDto(procedure));
    }

    @GetMapping("/by-name")
    public ResponseEntity<ProcedureResponseDto> getProcedureByName(
            @RequestParam String name) {
        Procedure procedure = findProcedureUseCase.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Procedimiento no encontrado con nombre: " + name
                ));
        return ResponseEntity.ok(ProcedureWebMapper.toDto(procedure));
    }

    @GetMapping
    public ResponseEntity<List<ProcedureResponseDto>> getAllProcedures() {
        List<Procedure> procedures = findProcedureUseCase.findAll();
        return ResponseEntity.ok(ProcedureWebMapper.toDtoList(procedures));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcedureResponseDto> updateProcedure(
            @PathVariable Long id,
            @RequestBody @Valid UpdateProcedureCommand command
    ) {
        UpdateProcedureCommand adjusted = new UpdateProcedureCommand(
                id,
                command.name(),
                command.cost()
        );

        Procedure updated = updateProcedureUseCase.updateProcedure(adjusted);
        return ResponseEntity.ok(ProcedureWebMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcedureById(@PathVariable Long id) {
        deleteProcedureUseCase.deleteProcedure(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by-name")
    public ResponseEntity<Void> deleteProcedureByName(@RequestParam String name) {
        deleteProcedureUseCase.deleteProcedureByName(name);
        return ResponseEntity.noContent().build();
    }
}

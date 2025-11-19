// File: src/main/java/app/infrastructure/web/StaffRestController.java
package app.infrastructure.web;

import app.application.port.in.CreateStaffCommand;
import app.application.port.in.UpdateStaffCommand;
import app.application.usecases.HHRRUseCase;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.Staff;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffRestController {

    private final HHRRUseCase.CreateStaffUseCase createStaffUseCase;
    private final HHRRUseCase.UpdateStaffUseCase updateStaffUseCase;
    private final HHRRUseCase.DeleteStaffUseCase deleteStaffUseCase;
    private final HHRRUseCase.FindStaffUseCase findStaffUseCase;

    public StaffRestController(HHRRUseCase.CreateStaffUseCase createStaffUseCase,
                               HHRRUseCase.UpdateStaffUseCase updateStaffUseCase,
                               HHRRUseCase.DeleteStaffUseCase deleteStaffUseCase,
                               HHRRUseCase.FindStaffUseCase findStaffUseCase) {
        this.createStaffUseCase = createStaffUseCase;
        this.updateStaffUseCase = updateStaffUseCase;
        this.deleteStaffUseCase = deleteStaffUseCase;
        this.findStaffUseCase = findStaffUseCase;
    }


    @PreAuthorize("hasRole('HUMAN_RESOURCES')")
    @PostMapping
    public ResponseEntity<Staff> createStaff(@Valid @RequestBody CreateStaffCommand command) {
        Staff created = createStaffUseCase.createStaff(command);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }


    @PreAuthorize("hasAnyRole('HUMAN_RESOURCES','ADMINISTRATIVE_STAFF')")
    @GetMapping
    public ResponseEntity<List<Staff>> getAllStaff() {
        List<Staff> staffList = findStaffUseCase.findAllStaff();
        return ResponseEntity.ok(staffList);
    }


    @PreAuthorize("hasAnyRole('HUMAN_RESOURCES','ADMINISTRATIVE_STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Long id) {
        Staff staff = findStaffUseCase.findStaffById(id);
        return ResponseEntity.ok(staff);
    }


    @PreAuthorize("hasAnyRole('HUMAN_RESOURCES','ADMINISTRATIVE_STAFF')")
    @GetMapping("/by-national-id/{nationalId}")
    public ResponseEntity<Staff> getStaffByNationalId(@PathVariable String nationalId) {
        Staff staff = findStaffUseCase.findStaffByNationalId(nationalId);
        return ResponseEntity.ok(staff);
    }

    @PreAuthorize("hasRole('HUMAN_RESOURCES')")
    @PutMapping("/by-national-id/{nationalId}")
    public ResponseEntity<Staff> updateStaffByNationalId(@PathVariable String nationalId,
                                                         @Valid @RequestBody UpdateStaffCommand command) {
        UpdateStaffCommand adjusted = new UpdateStaffCommand(
                nationalId,
                command.email(),
                command.phoneNumber(),
                command.address(),
                command.role(),
                command.password()
        );

        Staff updated = updateStaffUseCase.updateStaff(adjusted);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('HUMAN_RESOURCES')")
    @DeleteMapping("/by-national-id/{nationalId}")
    public ResponseEntity<Void> deleteStaffByNationalId(@PathVariable String nationalId) {
        deleteStaffUseCase.deleteStaff(nationalId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}

package app.infrastructure.web;

import app.application.port.in.CreateStaffCommand;
import app.application.port.in.AuthenticationCommand;
import app.application.port.in.AuthenticationPort;
import app.application.usecases.HHRRUseCase;
import app.domain.model.Staff;
import app.domain.model.enums.StaffRole;
import app.infrastructure.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final HHRRUseCase.CreateStaffUseCase createStaffUseCase;
    private final AuthenticationPort authenticationPort;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthRestController(HHRRUseCase.CreateStaffUseCase createStaffUseCase,
                              AuthenticationPort authenticationPort,
                              JwtTokenProvider jwtTokenProvider) {
        this.createStaffUseCase = createStaffUseCase;
        this.authenticationPort = authenticationPort;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<StaffResponse> register(@RequestBody @Valid CreateStaffCommand command) {
        Staff created = createStaffUseCase.createStaff(command);

        StaffResponse response = new StaffResponse(
                created.getId(),
                created.getFullName(),
                created.getUsername().getValue(),
                created.getRole()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationCommand command) {
        try {

            Staff staff = authenticationPort.authenticate(
                    command.getUsername(),
                    command.getPassword()
            );


            String token = jwtTokenProvider.generateToken(staff);

            LoginResponse response = new LoginResponse(
                    token,
                    staff.getFullName(),
                    staff.getRole()
            );

            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException | BadCredentialsException ex) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }

    public record StaffResponse(
            Long id,
            String fullName,
            String username,
            StaffRole role
    ) {}

    public record LoginResponse(
            String token,
            String fullName,
            StaffRole role
    ) {}
}

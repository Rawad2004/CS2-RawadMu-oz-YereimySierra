package app.application.usecase;

import app.application.port.in.AuthenticateUseCase;
import app.domain.model.Staff;
import app.domain.model.vo.Username;
import app.domain.repository.StaffRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements AuthenticateUseCase {
    private final StaffRepositoryPort staffRepository;

    public AuthenticationService(StaffRepositoryPort staffRepository){
        this.staffRepository = staffRepository;
    }

    @Override
    public Staff authenticate(String username, String password){
        // Buscar usuario por username
        return staffRepository.findByUsername(new Username(username))
                .filter(staff -> staff.getPassword().matches(password)) // Asumiendo que PasswordVO tiene un método matches()
                .orElseThrow(() -> new SecurityException("Invalid credentials"));
    }

}

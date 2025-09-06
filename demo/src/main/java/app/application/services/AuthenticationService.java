package app.application.services;

import app.application.usecases.SupportUseCases.AuthenticateUseCase;
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
                .filter(staff -> staff.getPassword().matches(password))
                .orElseThrow(() -> new SecurityException("Invalid credentials"));
    }

}

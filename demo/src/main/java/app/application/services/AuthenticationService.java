package app.application.services;

import app.application.port.in.AuthenticationPort;
import app.domain.model.Staff;
import app.domain.model.vo.Username;
import app.domain.repository.StaffRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticationService implements AuthenticationPort {

    private final StaffRepositoryPort staffRepository;

    public AuthenticationService(StaffRepositoryPort staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public Staff authenticate(String username, String password) {
        return staffRepository.findByUsername(new Username(username))
                .filter(staff -> staff.getPassword().matches(password))
                .orElseThrow(() -> new SecurityException("Credenciales inválidas"));
    }
}

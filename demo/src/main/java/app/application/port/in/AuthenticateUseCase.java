package app.application.port.in;

import app.domain.model.Staff;

public interface AuthenticateUseCase {
    Staff authenticate(String username, String password);
}

package app.application.port.in;

import app.domain.model.Staff;

public interface AuthenticationPort {
    Staff authenticate(String username, String password);
}
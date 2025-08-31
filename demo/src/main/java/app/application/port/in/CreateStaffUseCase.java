package app.application.port.in;

import app.domain.model.Staff;

public interface CreateStaffUseCase {
    Staff createStaff(CreateStaffCommand command);
}
package app.application.usecases;

import app.application.dto.CreateStaffCommand;
import app.application.dto.UpdateStaffCommand;
import app.domain.model.Staff;

import java.util.List;

public interface HHRRUseCase {
    interface CreateStaffUseCase{
        Staff createStaff(CreateStaffCommand command);
    }
    interface UpdateStaffUseCase {
        Staff updateStaff(UpdateStaffCommand command);
    }

    interface DeleteStaffUseCase {
        void deleteStaff(String nationalId);
    }
    interface FindStaffUseCase {
        Staff findStaffById(Long id);
        Staff findStaffByNationalId(String nationalId);
        List<Staff> findAllStaff();
    }
}

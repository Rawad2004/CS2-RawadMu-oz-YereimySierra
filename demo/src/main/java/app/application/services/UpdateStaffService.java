package app.application.services;

import app.application.port.in.UpdateStaffCommand;
import app.application.usecases.HHRRUseCase;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.Staff;
import app.domain.model.enums.StaffRole;
import app.domain.model.vo.*;
import app.domain.repository.StaffRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateStaffService implements HHRRUseCase.UpdateStaffUseCase {

    private final StaffRepositoryPort staffRepository;

    public UpdateStaffService(StaffRepositoryPort staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public Staff updateStaff(UpdateStaffCommand command) {
        NationalId nationalId = new NationalId(command.nationalId());
        Staff existingStaff = staffRepository.findByNationalId(nationalId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró empleado con cédula: " + command.nationalId()));

        Email newEmail = (command.email() != null && !command.email().isBlank())
                ? new Email(command.email()) : existingStaff.getEmail();

        PhoneNumber newPhone = (command.phoneNumber() != null && !command.phoneNumber().isBlank())
                ? new PhoneNumber(command.phoneNumber()) : existingStaff.getPhoneNumber();

        Address newAddress = (command.address() != null && !command.address().isBlank())
                ? new Address(command.address()) : existingStaff.getAddress();


        StaffRole newRole = command.role() != null ? command.role() : existingStaff.getRole();

        existingStaff.updateContactInfo(newAddress, newPhone, newEmail);


        existingStaff.changeRole(newRole);


        if (command.password() != null && !command.password().isBlank()) {
            existingStaff.changePassword(new Password(command.password()));
        }

        return staffRepository.save(existingStaff);
    }

}


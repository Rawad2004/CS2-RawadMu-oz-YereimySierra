// File: src/main/java/app/application/services/UpdateStaffService.java
package app.application.services;

import app.application.dto.UpdateStaffCommand;
import app.application.usecases.HHRRUseCase.UpdateStaffUseCase;
import app.domain.model.Staff;
import app.domain.model.vo.Address;
import app.domain.model.vo.Email;
import app.domain.model.vo.NationalId;
import app.domain.model.vo.Password;
import app.domain.model.vo.PhoneNumber;
import app.domain.repository.StaffRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateStaffService implements UpdateStaffUseCase {

    private final StaffRepositoryPort staffRepository;

    public UpdateStaffService(StaffRepositoryPort staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public Staff updateStaff(UpdateStaffCommand command) {
        Staff existingStaff = staffRepository.findByNationalId(new NationalId(command.nationalId()))
                .orElseThrow(() -> new IllegalArgumentException("Staff no encontrado con cédula: " + command.nationalId()));

        // Validar que no se intente cambiar el rol
        if (command.role() != null && !command.role().equals(existingStaff.getRole())) {
            throw new IllegalArgumentException("No está permitido cambiar el rol de un usuario existente");
        }

        // Actualizar campos permitidos si se proporcionan nuevos valores
        if (command.email() != null) {
            updateContactInfo(existingStaff, command);
        } else if (command.address() != null || command.phoneNumber() != null) {
            updateContactInfo(existingStaff, command);
        }

        // Actualizar password si se proporciona (y es diferente)
        if (command.password() != null && !command.password().trim().isEmpty()) {
            updatePassword(existingStaff, command.password());
        }

        return staffRepository.save(existingStaff);
    }

    private void updateContactInfo(Staff staff, UpdateStaffCommand command) {
        Address newAddress = command.address() != null ?
                new Address(command.address()) : staff.getAddress();

        PhoneNumber newPhoneNumber = command.phoneNumber() != null ?
                new PhoneNumber(command.phoneNumber()) : staff.getPhoneNumber();

        Email newEmail = command.email() != null ?
                new Email(command.email()) : staff.getEmail();

        staff.updateContactInfo(newAddress, newPhoneNumber, newEmail);
    }

    private void updatePassword(Staff staff, String newPassword) {
        // Validar que la nueva contraseña sea diferente a la actual
        if (!staff.getPassword().matches(newPassword)) {
            staff.changePassword(new Password(newPassword));
        }
        // Si es la misma, no hacemos nada
    }
}
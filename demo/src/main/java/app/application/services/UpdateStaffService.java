package app.application.services;

import app.application.port.in.UpdateStaffCommand;
import app.application.usecases.HHRRUseCase;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.Staff;
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
        // 1. Buscamos al empleado por CÉDULA, como lo define el Command
        NationalId nationalId = new NationalId(command.nationalId());
        Staff existingStaff = staffRepository.findByNationalId(nationalId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró empleado con cédula: " + command.nationalId()));

        // 2. Preparamos los nuevos datos, manteniendo los viejos si no se proporciona un nuevo valor
        Email newEmail = (command.email() != null && !command.email().isBlank())
                ? new Email(command.email()) : existingStaff.getEmail();

        PhoneNumber newPhone = (command.phoneNumber() != null && !command.phoneNumber().isBlank())
                ? new PhoneNumber(command.phoneNumber()) : existingStaff.getPhoneNumber();

        Address newAddress = (command.address() != null && !command.address().isBlank())
                ? new Address(command.address()) : existingStaff.getAddress();

        // 3. Llamamos al método de la entidad para actualizar la información de contacto
        existingStaff.updateContactInfo(newAddress, newPhone, newEmail);

        // 4. Actualizamos la contraseña SÓLO si se proporcionó una nueva
        if (command.password() != null && !command.password().isBlank()) {
            existingStaff.changePassword(new Password(command.password()));
        }

        // 5. Guardamos y devolvemos la entidad actualizada
        return staffRepository.save(existingStaff);
    }
}


package app.application.services;

import app.application.port.in.CreateStaffCommand;
import app.application.usecases.HHRRUseCase.CreateStaffUseCase;
import app.domain.model.Staff;
import app.domain.model.vo.*;
import app.domain.repository.StaffRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateStaffService implements CreateStaffUseCase {

    private final StaffRepositoryPort staffRepository;

    public CreateStaffService(StaffRepositoryPort staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public Staff createStaff(CreateStaffCommand command) {
        NationalId nationalId = new NationalId(command.nationalId());
        if (staffRepository.existsByNationalId(nationalId)) {
            throw new IllegalStateException("Staff member already exists with this national ID.");
        }

        Username username = new Username(command.username());
        if (staffRepository.existsByUsername(username)) {
            throw new IllegalStateException("Username already exists.");
        }

        Staff newStaff = new Staff(
                command.fullName(),
                nationalId,
                new Email(command.email()),
                new PhoneNumber(command.phoneNumber()),
                new DateOfBirth(command.birthDate()),
                new Address(command.address()),
                command.role(),
                username,
                new Password(command.password())
        );

        return staffRepository.save(newStaff);
    }
}
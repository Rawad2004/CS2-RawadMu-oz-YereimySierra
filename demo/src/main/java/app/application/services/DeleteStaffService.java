package app.application.services;

import app.application.usecases.HHRRUseCase.DeleteStaffUseCase;
import app.domain.model.vo.NationalId;
import app.domain.repository.StaffRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteStaffService implements DeleteStaffUseCase {

    private final StaffRepositoryPort staffRepository;

    public DeleteStaffService(StaffRepositoryPort staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public void deleteStaff(String nationalId) {
        if (!staffRepository.existsByNationalId(new NationalId(nationalId))) {
            throw new IllegalArgumentException("Staff no encontrado con cédula: " + nationalId);
        }


        staffRepository.deleteById(
                staffRepository.findByNationalId(new NationalId(nationalId))
                        .orElseThrow(() -> new IllegalArgumentException("Staff no encontrado"))
                        .getId()
        );
    }
}

// File: src/main/java/app/application/services/DeleteStaffService.java
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
        // Validar que el staff exista
        if (!staffRepository.existsByNationalId(new NationalId(nationalId))) {
            throw new IllegalArgumentException("Staff no encontrado con cédula: " + nationalId);
        }

        // Aquí se pueden agregar validaciones adicionales antes de eliminar
        // Por ejemplo: verificar que no tenga registros asociados, etc.

        staffRepository.deleteById(
                staffRepository.findByNationalId(new NationalId(nationalId))
                        .orElseThrow(() -> new IllegalArgumentException("Staff no encontrado"))
                        .getId()
        );
    }
}

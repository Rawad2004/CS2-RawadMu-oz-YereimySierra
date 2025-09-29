package app.application.services;

import app.application.usecases.HHRRUseCase.FindStaffUseCase;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.Staff;
import app.domain.model.vo.NationalId;
import app.domain.repository.StaffRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// Para operaciones de solo lectura, es una buena práctica añadir (readOnly = true)
// Es una optimización para la base de datos.
@Transactional(readOnly = true)
public class FindStaffService implements FindStaffUseCase {

    private final StaffRepositoryPort staffRepository;

    public FindStaffService(StaffRepositoryPort staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public Staff findStaffById(Long id) {
        // Llama al puerto del repositorio y, si no encuentra nada, lanza nuestra excepción personalizada.
        return staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con ID: " + id));
    }

    @Override
    public Staff findStaffByNationalId(String nationalId) {
        // Primero creamos el VO para asegurar que el formato de la cédula es válido.
        NationalId id = new NationalId(nationalId);

        // Luego buscamos y manejamos el caso de que no se encuentre.
        return staffRepository.findByNationalId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con Cédula: " + nationalId));
    }

    @Override
    public List<Staff> findAllStaff() {

        return staffRepository.findAll();
    }
}
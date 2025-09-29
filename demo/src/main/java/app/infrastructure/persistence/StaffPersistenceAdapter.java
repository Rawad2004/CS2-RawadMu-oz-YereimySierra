package app.infrastructure.persistence;

import app.domain.model.Staff;
import app.domain.model.vo.NationalId;
import app.domain.model.vo.Username;
import app.domain.repository.StaffRepositoryPort;
import app.infrastructure.persistence.jpa.StaffJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StaffPersistenceAdapter implements StaffRepositoryPort {

    private final StaffJpaRepository staffJpaRepository;

    public StaffPersistenceAdapter(StaffJpaRepository staffJpaRepository) {
        this.staffJpaRepository = staffJpaRepository;
    }

    @Override
    public Staff save(Staff staff) {
        return staffJpaRepository.save(staff);
    }

    @Override
    public Optional<Staff> findById(Long id) {
        return staffJpaRepository.findById(id);
    }

    @Override
    public List<Staff> findAll() {
        return staffJpaRepository.findAll();
    }

    @Override
    public Optional<Staff> findByUsername(Username username) {
        return staffJpaRepository.findByUsername(username);
    }

    @Override
    public Optional<Staff> findByNationalId(NationalId nationalId) {
        return staffJpaRepository.findByNationalId(nationalId);
    }

    @Override
    public void deleteById(Long id) {
        staffJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(Username username) {
        return staffJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByNationalId(NationalId nationalId) {
        return staffJpaRepository.existsByNationalId(nationalId);
    }
}

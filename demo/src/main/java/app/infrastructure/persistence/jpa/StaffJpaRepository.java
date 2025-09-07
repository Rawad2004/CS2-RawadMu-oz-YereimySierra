package app.infrastructure.persistence.jpa;

import app.domain.model.Staff;
import app.domain.model.vo.NationalId;
import app.domain.model.vo.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StaffJpaRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByUsername(Username username);
    Optional<Staff> findByNationalId(NationalId nationalId);
    boolean existsByUsername(Username username);
    boolean existsByNationalId(NationalId nationalId);
}
package app.infrastructure.persistence.jpa;

import app.domain.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MedicationJpaRepository extends JpaRepository<Medication, Long> {

    /**
     * Spring Data JPA implementará este método automáticamente para buscar
     * un medicamento por su campo 'name'.
     * @param name El nombre del medicamento a buscar.
     * @return Un Optional que contiene el medicamento si se encuentra.
     */
    Optional<Medication> findByName(String name);
}
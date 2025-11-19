package app.domain.repository;

import app.domain.model.Medication;

import java.util.List;
import java.util.Optional;

public interface MedicationRepositoryPort {

    Medication save(Medication medication);

    Optional<Medication> findById(Long id);

    Optional<Medication> findByName(String name);

    List<Medication> findAll();

    void deleteById(Long id);

    void deleteByName(String name);

    boolean existsById(Long id);

    boolean existsByName(String name);
}

package app.infrastructure.persistence;

import app.domain.model.Medication;
import app.domain.repository.MedicationRepositoryPort;
import app.infrastructure.persistence.jpa.MedicationJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MedicationPersistenceAdapter implements MedicationRepositoryPort {

    private final MedicationJpaRepository medicationJpaRepository;

    public MedicationPersistenceAdapter(MedicationJpaRepository medicationJpaRepository) {
        this.medicationJpaRepository = medicationJpaRepository;
    }

    @Override
    public Medication save(Medication medication) {
        return medicationJpaRepository.save(medication);
    }

    @Override
    public Optional<Medication> findById(Long id) {
        return medicationJpaRepository.findById(id);
    }

    @Override
    public Optional<Medication> findByName(String name) {
        return medicationJpaRepository.findByName(name);
    }

    @Override
    public List<Medication> findAll() {
        return medicationJpaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        medicationJpaRepository.deleteById(id);
    }
}
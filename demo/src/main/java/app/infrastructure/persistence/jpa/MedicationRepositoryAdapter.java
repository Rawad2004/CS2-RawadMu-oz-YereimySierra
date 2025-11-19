package app.infrastructure.persistence.jpa;

import app.domain.model.Medication;
import app.domain.model.vo.Money;
import app.domain.repository.MedicationRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MedicationRepositoryAdapter implements MedicationRepositoryPort {

    private final MedicationJpaRepository jpaRepository;

    public MedicationRepositoryAdapter(MedicationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Medication save(Medication medication) {
        return jpaRepository.save(medication);
    }

    @Override
    public Optional<Medication> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Medication> findByName(String name) {
        return jpaRepository.findByName(name);
    }

    @Override
    public List<Medication> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteByName(String name) {
        findByName(name).ifPresent(jpaRepository::delete);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.findByName(name).isPresent();
    }
}

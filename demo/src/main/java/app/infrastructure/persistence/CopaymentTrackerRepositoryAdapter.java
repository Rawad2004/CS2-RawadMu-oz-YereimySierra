package app.infrastructure.persistence;

import app.domain.model.CopaymentTracker;
import app.domain.repository.CopaymentTrackerRepositoryPort;
import app.infrastructure.persistence.jpa.CopaymentTrackerJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CopaymentTrackerRepositoryAdapter implements CopaymentTrackerRepositoryPort {

    private final CopaymentTrackerJpaRepository jpaRepository;

    public CopaymentTrackerRepositoryAdapter(CopaymentTrackerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public CopaymentTracker save(CopaymentTracker tracker) {
        return jpaRepository.save(tracker);
    }

    @Override
    public Optional<CopaymentTracker> findByPatientAndFiscalYear(String patientNationalId, int fiscalYear) {
        return jpaRepository.findByPatientNationalIdAndFiscalYear(patientNationalId, fiscalYear);
    }

    @Override
    public CopaymentTracker findOrCreateByPatientAndFiscalYear(String patientNationalId, String patientName, int fiscalYear) {
        return jpaRepository.findByPatientNationalIdAndFiscalYear(patientNationalId, fiscalYear)
                .orElseGet(() -> jpaRepository.save(new CopaymentTracker(patientNationalId, patientName, fiscalYear)));
    }

    @Override
    public List<CopaymentTracker> findByFiscalYear(int fiscalYear) {
        return jpaRepository.findByFiscalYear(fiscalYear);
    }
}

package app.infrastructure.persistence;

import app.domain.model.CopaymentTracker;
import app.domain.repository.CopaymentTrackerRepositoryPort;
import app.infrastructure.persistence.jpa.CopaymentTrackerJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CopaymentTrackerPersistenceAdapter implements CopaymentTrackerRepositoryPort {

    private final CopaymentTrackerJpaRepository copaymentTrackerJpaRepository;

    public CopaymentTrackerPersistenceAdapter(CopaymentTrackerJpaRepository copaymentTrackerJpaRepository) {
        this.copaymentTrackerJpaRepository = copaymentTrackerJpaRepository;
    }

    @Override
    public CopaymentTracker save(CopaymentTracker tracker) {
        return copaymentTrackerJpaRepository.save(tracker);
    }

    @Override
    public Optional<CopaymentTracker> findByPatientAndFiscalYear(String patientNationalId, int fiscalYear) {
        return copaymentTrackerJpaRepository.findByPatientNationalIdAndFiscalYear(patientNationalId, fiscalYear);
    }

    @Override
    public CopaymentTracker findOrCreateByPatientAndFiscalYear(String patientNationalId, String patientName, int fiscalYear) {
        return copaymentTrackerJpaRepository.findByPatientNationalIdAndFiscalYear(patientNationalId, fiscalYear)
                .orElseGet(() -> {
                    CopaymentTracker newTracker = new CopaymentTracker(patientNationalId, patientName, fiscalYear);
                    return copaymentTrackerJpaRepository.save(newTracker);
                });
    }

    @Override
    public List<CopaymentTracker> findByFiscalYear(int fiscalYear) {
        return copaymentTrackerJpaRepository.findByFiscalYear(fiscalYear);
    }
}
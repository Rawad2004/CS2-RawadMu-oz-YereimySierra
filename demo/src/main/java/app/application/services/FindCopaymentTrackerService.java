// File: src/main/java/app/application/services/FindCopaymentTrackerService.java
package app.application.services;

import app.application.usecases.BillingUseCases;
import app.domain.model.CopaymentTracker;
import app.domain.repository.CopaymentTrackerRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FindCopaymentTrackerService implements BillingUseCases.FindCopaymentTrackerUseCase {

    private final CopaymentTrackerRepositoryPort copaymentTrackerRepository;

    public FindCopaymentTrackerService(CopaymentTrackerRepositoryPort copaymentTrackerRepository) {
        this.copaymentTrackerRepository = copaymentTrackerRepository;
    }

    @Override
    public Optional<CopaymentTracker> findByPatientAndFiscalYear(String patientNationalId, int fiscalYear) {
        return copaymentTrackerRepository.findByPatientAndFiscalYear(patientNationalId, fiscalYear);
    }

    @Override
    public List<CopaymentTracker> findByFiscalYear(int fiscalYear) {
        return copaymentTrackerRepository.findByFiscalYear(fiscalYear);
    }
}

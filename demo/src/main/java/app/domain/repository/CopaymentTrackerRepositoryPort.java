package app.domain.repository;

import app.domain.model.CopaymentTracker;
import java.util.List;
import java.util.Optional;

public interface CopaymentTrackerRepositoryPort {
    CopaymentTracker save(CopaymentTracker tracker);
    Optional<CopaymentTracker> findByPatientAndFiscalYear(String patientNationalId, int fiscalYear);
    CopaymentTracker findOrCreateByPatientAndFiscalYear(String patientNationalId, String patientName, int fiscalYear);
    List<CopaymentTracker> findByFiscalYear(int fiscalYear);
}

package app.infrastructure.persistence.jpa;

import app.domain.model.CopaymentTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CopaymentTrackerJpaRepository extends JpaRepository<CopaymentTracker, Long> {

    @Query("SELECT ct FROM CopaymentTracker ct WHERE ct.patientNationalId = :patientNationalId AND ct.fiscalYear = :fiscalYear")
    Optional<CopaymentTracker> findByPatientNationalIdAndFiscalYear(@Param("patientNationalId") String patientNationalId,
                                                                    @Param("fiscalYear") int fiscalYear);

    @Query("SELECT ct FROM CopaymentTracker ct WHERE ct.fiscalYear = :fiscalYear")
    List<CopaymentTracker> findByFiscalYear(@Param("fiscalYear") int fiscalYear);

    @Query("SELECT COUNT(ct) > 0 FROM CopaymentTracker ct WHERE ct.patientNationalId = :patientNationalId AND ct.fiscalYear = :fiscalYear")
    boolean existsByPatientNationalIdAndFiscalYear(@Param("patientNationalId") String patientNationalId,
                                                   @Param("fiscalYear") int fiscalYear);
}

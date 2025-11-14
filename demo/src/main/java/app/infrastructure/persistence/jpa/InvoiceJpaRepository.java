package app.infrastructure.persistence.jpa;

import app.domain.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InvoiceJpaRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    List<Invoice> findByPatientNationalId(String patientNationalId);

    @Query("SELECT i FROM Invoice i WHERE i.patientNationalId = :patientNationalId AND i.fiscalYear = :fiscalYear")
    List<Invoice> findByPatientNationalIdAndFiscalYear(@Param("patientNationalId") String patientNationalId,
                                                       @Param("fiscalYear") int fiscalYear);

    boolean existsByInvoiceNumber(String invoiceNumber);
}
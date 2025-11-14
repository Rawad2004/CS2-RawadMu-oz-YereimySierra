package app.domain.repository;

import app.domain.model.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepositoryPort {
    Invoice save(Invoice invoice);
    Optional<Invoice> findById(Long id);
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    List<Invoice> findByPatientNationalId(String patientNationalId);
    List<Invoice> findByPatientNationalIdAndFiscalYear(String patientNationalId, int fiscalYear);
    List<Invoice> findAll();
    void deleteById(Long id);
    boolean existsByInvoiceNumber(String invoiceNumber);
}

package app.infrastructure.persistence;

import app.domain.model.Invoice;
import app.domain.repository.InvoiceRepositoryPort;
import app.infrastructure.persistence.jpa.InvoiceJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InvoicePersistenceAdapter implements InvoiceRepositoryPort {

    private final InvoiceJpaRepository invoiceJpaRepository;

    public InvoicePersistenceAdapter(InvoiceJpaRepository invoiceJpaRepository) {
        this.invoiceJpaRepository = invoiceJpaRepository;
    }

    @Override
    public Invoice save(Invoice invoice) {
        return invoiceJpaRepository.save(invoice);
    }

    @Override
    public Optional<Invoice> findById(Long id) {
        return invoiceJpaRepository.findById(id);
    }

    @Override
    public Optional<Invoice> findByInvoiceNumber(String invoiceNumber) {
        return invoiceJpaRepository.findByInvoiceNumber(invoiceNumber);
    }

    @Override
    public List<Invoice> findByPatientNationalId(String patientNationalId) {
        return invoiceJpaRepository.findByPatientNationalId(patientNationalId);
    }

    @Override
    public List<Invoice> findByPatientNationalIdAndFiscalYear(String patientNationalId, int fiscalYear) {
        return invoiceJpaRepository.findByPatientNationalIdAndFiscalYear(patientNationalId, fiscalYear);
    }

    @Override
    public List<Invoice> findAll() {
        return invoiceJpaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        invoiceJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByInvoiceNumber(String invoiceNumber) {
        return invoiceJpaRepository.existsByInvoiceNumber(invoiceNumber);
    }
}

package app.application.services;

import app.application.usecases.BillingUseCases;
import app.domain.model.Invoice;
import app.domain.repository.InvoiceRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FindInvoiceService implements BillingUseCases.FindInvoiceUseCase {

    private final InvoiceRepositoryPort invoiceRepository;

    public FindInvoiceService(InvoiceRepositoryPort invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Optional<Invoice> findByInvoiceNumber(String invoiceNumber) {
        if (invoiceNumber == null || invoiceNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de factura es obligatorio");
        }
        return invoiceRepository.findByInvoiceNumber(invoiceNumber);
    }

    @Override
    public List<Invoice> findByPatientNationalId(String nationalId) {
        if (nationalId == null || nationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        return invoiceRepository.findByPatientNationalId(nationalId);
    }

    @Override
    public List<Invoice> findByPatientAndFiscalYear(String nationalId, int fiscalYear) {
        if (nationalId == null || nationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        if (fiscalYear < 2000) {
            throw new IllegalArgumentException("Año fiscal inválido");
        }
        return invoiceRepository.findByPatientNationalIdAndFiscalYear(nationalId, fiscalYear);
    }

    @Override
    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }
}

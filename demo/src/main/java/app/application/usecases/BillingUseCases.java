package app.application.usecases;

import app.domain.model.Invoice;
import app.domain.model.CopaymentTracker;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillingUseCases {


    interface GenerateInvoiceUseCase {
        Invoice generateInvoice(String patientNationalId,
                                String orderNumber,
                                LocalDate invoiceDate);
    }


    interface FindInvoiceUseCase {
        Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
        List<Invoice> findByPatientNationalId(String patientNationalId);
        List<Invoice> findByPatientAndFiscalYear(String patientNationalId, int fiscalYear);
        List<Invoice> findAll();
    }


    interface FindCopaymentTrackerUseCase {

        Optional<CopaymentTracker> findByPatientAndFiscalYear(String patientNationalId, int fiscalYear);


        List<CopaymentTracker> findByFiscalYear(int fiscalYear);
    }


}

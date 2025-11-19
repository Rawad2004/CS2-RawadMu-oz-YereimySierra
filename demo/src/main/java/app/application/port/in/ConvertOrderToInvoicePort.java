package app.application.port.in;

import app.domain.model.Invoice;

import java.util.List;

public interface ConvertOrderToInvoicePort {

    Invoice convertOrderToInvoice(String orderNumber);


    List<Invoice> generateInvoicesForPatient(String patientNationalId, int fiscalYear);
}

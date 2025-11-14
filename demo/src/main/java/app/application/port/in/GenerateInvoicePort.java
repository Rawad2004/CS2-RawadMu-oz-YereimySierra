package app.application.port.in;

import app.domain.model.Invoice;

public interface GenerateInvoicePort {
    Invoice generateInvoice(GenerateInvoiceCommand command);
}

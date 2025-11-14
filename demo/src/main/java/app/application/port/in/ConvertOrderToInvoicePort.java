package app.application.port.in;

import app.domain.model.Invoice;

import java.util.List;

public interface ConvertOrderToInvoicePort {
    /**
     * Convierte una orden médica específica en una factura
     */
    Invoice convertOrderToInvoice(String orderNumber);

    /**
     * Genera facturas para todas las órdenes de un paciente en un año fiscal
     */
    List<Invoice> generateInvoicesForPatient(String patientNationalId, int fiscalYear);
}

package app.application.services;

import app.domain.model.Invoice;
import app.domain.model.Order;
import app.domain.repository.OrderRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderInvoiceTriggerService {

    private final OrderToInvoiceService orderToInvoiceService;
    private final OrderClinicalHistorySyncService clinicalHistorySyncService;
    private final CopaymentControlTriggerService copaymentTriggerService;
    private final DiagnosticFlowTriggerService diagnosticFlowTriggerService;
    private final OrderRepositoryPort orderRepository;

    public OrderInvoiceTriggerService(OrderToInvoiceService orderToInvoiceService,
                                      OrderClinicalHistorySyncService clinicalHistorySyncService,
                                      CopaymentControlTriggerService copaymentTriggerService,
                                      DiagnosticFlowTriggerService diagnosticFlowTriggerService,
                                      OrderRepositoryPort orderRepository) {
        this.orderToInvoiceService = orderToInvoiceService;
        this.clinicalHistorySyncService = clinicalHistorySyncService;
        this.copaymentTriggerService = copaymentTriggerService;
        this.diagnosticFlowTriggerService = diagnosticFlowTriggerService;
        this.orderRepository = orderRepository;
    }

    public void generateInvoiceAfterOrderCreation(String orderNumber) {
        try {

            Thread.sleep(100);


            orderToInvoiceService.convertOrderToInvoice(orderNumber);

            System.out.println("✅ Factura generada automáticamente para la orden: " + orderNumber);

        } catch (Exception e) {
            System.err.println("❌ Error generando factura automática para orden " + orderNumber + ": " + e.getMessage());
        }
    }


    public void onOrderCreated(Order order) {

        new Thread(() -> {
            try {

                clinicalHistorySyncService.syncOrderWithClinicalHistory(order);

                Invoice invoice = orderToInvoiceService.convertOrderToInvoice(order.getOrderNumber());

                copaymentTriggerService.updateCopaymentTracker(invoice);

                copaymentTriggerService.applyAutomaticCopaymentExemption(
                        invoice.getPatientNationalId(),
                        invoice.getFiscalYear()
                );

                System.out.println("🎯 Todos los triggers ejecutados para orden: " + order.getOrderNumber());

            } catch (Exception e) {
                System.err.println("❌ Error en triggers de orden: " + e.getMessage());
            }
        }).start();
    }
}
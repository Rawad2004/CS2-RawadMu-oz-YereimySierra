// File: src/main/java/app/application/services/OrderInvoiceTriggerService.java
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

    /**
     * Método que se llamará después de crear una orden para generar factura automáticamente
     */
    public void generateInvoiceAfterOrderCreation(String orderNumber) {
        try {
            // Esperar un momento para asegurar que la orden esté persistida
            Thread.sleep(100);

            // Generar factura automáticamente
            orderToInvoiceService.convertOrderToInvoice(orderNumber);

            System.out.println("✅ Factura generada automáticamente para la orden: " + orderNumber);

        } catch (Exception e) {
            System.err.println("❌ Error generando factura automática para orden " + orderNumber + ": " + e.getMessage());
            // No lanzamos la excepción para no interrumpir el flujo principal
        }
    }

    /**
     * Método para ser llamado desde el CreateOrderService después de guardar la orden
     */
    public void onOrderCreated(Order order) {
        // Ejecutar en un hilo separado para no bloquear la respuesta
        new Thread(() -> {
            try {
                // ✅ 1. Sincronizar con historia clínica
                clinicalHistorySyncService.syncOrderWithClinicalHistory(order);

                // ✅ 2. Generar factura
                Invoice invoice = orderToInvoiceService.convertOrderToInvoice(order.getOrderNumber());

                // ✅ 3. Actualizar control de copagos
                copaymentTriggerService.updateCopaymentTracker(invoice);

                // ✅ 4. Verificar exención automática
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
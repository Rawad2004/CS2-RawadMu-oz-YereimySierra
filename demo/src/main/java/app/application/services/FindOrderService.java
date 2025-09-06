// File: src/main/java/app/application/services/FindOrderService.java
package app.application.services;

import app.application.usecases.NurseUseCases;
import app.domain.model.Order;
import app.domain.model.vo.NationalId;
import app.domain.repository.OrderRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FindOrderService implements NurseUseCases.FindOrderUseCase {

    private final OrderRepositoryPort orderRepository;

    public FindOrderService(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Optional<Order> findOrderByOrderNumber(String orderNumber) {
        if (orderNumber == null || orderNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de orden no puede ser nulo o vacío");
        }
        return orderRepository.findByOrderNumber(orderNumber);
    }

    @Override
    public List<Order> findOrdersByPatientNationalId(String patientNationalId) {
        if (patientNationalId == null || patientNationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del paciente no puede ser nula o vacía");
        }

        NationalId nationalId = new NationalId(patientNationalId);
        return orderRepository.findByPatientId(nationalId); // ← Ahora funciona
    }

    @Override
    public List<Order> findOrdersByPatientNationalIdAndMedication(String patientNationalId, Long medicationId) {
        List<Order> patientOrders = findOrdersByPatientNationalId(patientNationalId);

        // Filtrar órdenes que contengan el medicamento específico
        return patientOrders.stream()
                .filter(order -> order.getItems().stream()
                        .anyMatch(item -> item instanceof app.domain.model.order.MedicationOrderItem &&
                                ((app.domain.model.order.MedicationOrderItem) item).getMedicationId().equals(medicationId)))
                .toList();
    }

    public List<Order> findActiveOrdersByPatient(String patientNationalId) {
        List<Order> patientOrders = findOrdersByPatientNationalId(patientNationalId);

        // Filtrar órdenes activas (implementar lógica según reglas de negocio)
        return patientOrders.stream()
                .filter(this::isOrderActive)
                .toList();
    }

    private boolean isOrderActive(Order order) {
        // Implementar lógica real según las reglas de negocio
        // Por ejemplo: órdenes de los últimos 7 días
        return order.getCreationDate().isAfter(java.time.LocalDate.now().minusDays(7));
    }
}
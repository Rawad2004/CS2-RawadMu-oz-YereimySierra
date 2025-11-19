// File: src/main/java/app/application/services/GetOrderDetailService.java
package app.application.services;

import app.application.usecases.DoctorUseCases;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.Order;
import app.domain.repository.OrderRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetOrderDetailService implements DoctorUseCases.GetOrderDetailUseCase {

    private final OrderRepositoryPort orderRepository;

    public GetOrderDetailService(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order getOrderByNumber(String orderNumber) {
        if (orderNumber == null || orderNumber.isBlank()) {
            throw new IllegalArgumentException("El número de orden es obligatorio");
        }

        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden no encontrada con número: " + orderNumber
                ));
    }
}

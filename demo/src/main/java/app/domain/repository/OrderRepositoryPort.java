package app.domain.repository;

import app.domain.model.Order;
import java.util.Optional;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findByOrderNumber(String orderNumber);
    Optional<Order> findById(Long id);
}
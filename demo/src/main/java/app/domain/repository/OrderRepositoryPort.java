// File: src/main/java/app/domain/repository/OrderRepositoryPort.java
package app.domain.repository;

import app.domain.model.Order;
import app.domain.model.vo.NationalId;
import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findByOrderNumber(String orderNumber);
    Optional<Order> findById(Long id);
    List<Order> findByPatientId(NationalId patientId); // ← Nuevo método
}
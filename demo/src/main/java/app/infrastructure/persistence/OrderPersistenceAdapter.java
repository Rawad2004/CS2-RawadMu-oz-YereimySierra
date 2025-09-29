package app.infrastructure.persistence;

import app.domain.model.Order;
import app.domain.model.vo.NationalId;
import app.domain.repository.OrderRepositoryPort;
import app.infrastructure.persistence.jpa.OrderJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderPersistenceAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;

    public OrderPersistenceAdapter(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderJpaRepository.findByOrderNumber(orderNumber);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id);
    }

    @Override
    public List<Order> findByPatientId(NationalId patientId) {

        return orderJpaRepository.findByPatientId_Value(patientId.getValue());
    }
}

package app.application.services;

import app.application.usecases.NurseUseCases.FindOrdersByProcedureUseCase;
import app.domain.model.Order;
import app.domain.model.order.ProcedureOrderItem;
import app.domain.model.vo.NationalId;
import app.domain.repository.OrderRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindOrdersByProcedureService implements FindOrdersByProcedureUseCase {

    private final OrderRepositoryPort orderRepository;

    public FindOrdersByProcedureService(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findOrdersByPatientNationalIdAndProcedure(String patientNationalId, Long procedureId) {
        List<Order> patientOrders = orderRepository.findByPatientId(new NationalId(patientNationalId));

        return patientOrders.stream()
                .filter(order -> order.getItems().stream()
                        .anyMatch(item -> {
                            if (item instanceof ProcedureOrderItem) {
                                ProcedureOrderItem procItem = (ProcedureOrderItem) item;
                                return procItem.getProcedureId().equals(procedureId);
                            }
                            return false;
                        }))
                .collect(Collectors.toList());
    }
}
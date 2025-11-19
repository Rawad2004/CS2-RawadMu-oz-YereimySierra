package app.application.services;

import app.application.usecases.DoctorUseCases;
import app.domain.model.Order;
import app.domain.model.vo.NationalId;
import app.domain.repository.OrderRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetPatientOrdersService implements DoctorUseCases.GetPatientOrdersUseCase {

    private final OrderRepositoryPort orderRepository;

    public GetPatientOrdersService(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getOrders(String patientNationalId) {
        if (patientNationalId == null || patientNationalId.isBlank()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }

        return orderRepository.findByPatientId(new NationalId(patientNationalId));
    }
}

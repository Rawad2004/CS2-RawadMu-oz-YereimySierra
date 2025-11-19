package app.application.services;

import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.Order;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import app.domain.repository.OrderRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DiagnosticFlowTriggerService {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;
    private final OrderRepositoryPort orderRepository;

    public DiagnosticFlowTriggerService(ClinicalHistoryRepositoryPort clinicalHistoryRepository,
                                        OrderRepositoryPort orderRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
        this.orderRepository = orderRepository;
    }


}

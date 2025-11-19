package app.application.services;

import app.application.port.in.AssociateOrderToVisitCommand;
import app.application.usecases.DoctorUseCases;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class AssociateOrderToVisitService implements DoctorUseCases.AssociateOrderToVisitUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public AssociateOrderToVisitService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public void associateOrder(String patientNationalId,
                               LocalDate visitDate,
                               AssociateOrderToVisitCommand command) {

        if (patientNationalId == null || patientNationalId.isBlank()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        if (visitDate == null) {
            throw new IllegalArgumentException("La fecha de la visita es obligatoria");
        }
        if (command == null) {
            throw new IllegalArgumentException("Los datos de la orden son obligatorios");
        }

        ClinicalHistoryEntry.OrderType orderType;
        try {
            orderType = ClinicalHistoryEntry.OrderType.valueOf(command.orderType());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Tipo de orden inválido: " + command.orderType(), ex);
        }

        clinicalHistoryRepository.associateOrderToVisit(
                patientNationalId,
                visitDate,
                command.orderNumber(),
                orderType
        );
    }
}

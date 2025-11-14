// File: src/main/java/app/application/services/RecordProcedureAdministrationService.java
package app.application.services;

import app.application.port.in.RecordProcedureAdministrationCommand;
import app.application.usecases.NurseUseCases.RecordProcedureAdministrationUseCase;
import app.domain.model.*;
import app.domain.model.order.ProcedureOrderItem;
import app.domain.model.vo.NationalId;
import app.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RecordProcedureAdministrationService implements RecordProcedureAdministrationUseCase {

    private final PatientRepositoryPort patientRepository;
    private final ProcedureRepositoryPort procedureRepository;
    private final OrderRepositoryPort orderRepository;

    public RecordProcedureAdministrationService(PatientRepositoryPort patientRepository,
                                                ProcedureRepositoryPort procedureRepository,
                                                OrderRepositoryPort orderRepository) {
        this.patientRepository = patientRepository;
        this.procedureRepository = procedureRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Patient recordProcedureAdministration(String patientNationalId, RecordProcedureAdministrationCommand command) {
        // 1. Validar que el paciente existe
        Patient patient = patientRepository.findByNationalId(new NationalId(patientNationalId))
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado con cédula: " + patientNationalId));

        // 2. Validar que el procedimiento existe
        Procedure procedure = procedureRepository.findById(command.procedureId())
                .orElseThrow(() -> new IllegalArgumentException("Procedimiento no encontrado con ID: " + command.procedureId()));

        // 3. Validar que la hora del procedimiento no sea futura
        if (command.procedureTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La hora del procedimiento no puede ser futura");
        }

        // 4. Validar que existe una orden activa con este procedimiento
        validateActiveProcedureOrder(patientNationalId, command.procedureId());

        // 5. Registrar el procedimiento
        System.out.println("✅ Procedimiento registrado - Paciente: " + patientNationalId +
                ", Procedimiento: " + procedure.getName() +
                ", Hora: " + command.procedureTime() +
                ", Observaciones: " + command.observations());

        return patient;
    }

    private void validateActiveProcedureOrder(String patientNationalId, Long procedureId) {
        List<Order> patientOrders = orderRepository.findByPatientId(new NationalId(patientNationalId));

        boolean hasActiveOrder = patientOrders.stream()
                .filter(order -> !order.getCreationDate().isBefore(LocalDateTime.now().minusMonths(1).toLocalDate()))
                .anyMatch(order -> order.getItems().stream()
                        .anyMatch(item -> {
                            if (item instanceof ProcedureOrderItem) {
                                ProcedureOrderItem procItem = (ProcedureOrderItem) item;
                                return procItem.getProcedureId().equals(procedureId);
                            }
                            return false;
                        }));

        if (!hasActiveOrder) {
            throw new IllegalStateException(
                    "No existe una orden activa que autorice este procedimiento para el paciente: " + patientNationalId
            );
        }
    }
}
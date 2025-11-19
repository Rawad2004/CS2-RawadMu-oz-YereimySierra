package app.application.services;

import app.application.events.OrderCreatedEvent;
import app.application.port.in.CreateOrderCommand;
import app.application.usecases.DoctorUseCases.CreateOrderUseCase;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.Order;
import app.domain.model.Patient;
import app.domain.model.Staff;
import app.domain.model.enums.OrderItemType;
import app.domain.model.enums.StaffRole;
import app.domain.model.order.DiagnosticAidOrderItem;
import app.domain.model.order.MedicationOrderItem;
import app.domain.model.order.OrderItem;
import app.domain.model.order.ProcedureOrderItem;
import app.domain.model.vo.NationalId;
import app.domain.repository.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CreateOrderService implements CreateOrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final PatientRepositoryPort patientRepository;
    private final StaffRepositoryPort staffRepository;
    private final MedicationRepositoryPort medicationRepository;
    private final ProcedureRepositoryPort procedureRepository;
    private final DiagnosticAidRepositoryPort diagnosticAidRepository;
    private final SpecialistRepositoryPort specialistRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CreateOrderService(OrderRepositoryPort orderRepository,
                              PatientRepositoryPort patientRepository,
                              StaffRepositoryPort staffRepository,
                              MedicationRepositoryPort medicationRepository,
                              ProcedureRepositoryPort procedureRepository,
                              DiagnosticAidRepositoryPort diagnosticAidRepository,
                              SpecialistRepositoryPort specialistRepository,
                              ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.patientRepository = patientRepository;
        this.staffRepository = staffRepository;
        this.medicationRepository = medicationRepository;
        this.procedureRepository = procedureRepository;
        this.diagnosticAidRepository = diagnosticAidRepository;
        this.specialistRepository = specialistRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Order createOrder(CreateOrderCommand command) {

        NationalId patientId = new NationalId(command.patientNationalId());
        Optional<Patient> patientOptional = patientRepository.findByNationalId(patientId);
        if (patientOptional.isEmpty()) {
            throw new ResourceNotFoundException("Paciente no encontrado con la cédula: " + patientId.getValue());
        }

        NationalId doctorId = new NationalId(command.doctorNationalId());
        Optional<Staff> staffOptional = staffRepository.findByNationalId(doctorId);
        if (staffOptional.isEmpty()) {
            throw new ResourceNotFoundException("No se encontró personal con la cédula: " + doctorId.getValue());
        }
        Staff doctor = staffOptional.get();
        if (doctor.getRole() != StaffRole.DOCTOR) {
            throw new IllegalStateException("El personal asignado a la orden no es un médico.");
        }

        if (orderRepository.findByOrderNumber(command.orderNumber()).isPresent()) {
            throw new IllegalStateException("El número de orden ya existe.");
        }

        Order order = new Order(command.orderNumber(), patientId, doctorId);

        validateOrderItemCompatibility(command.items());

        for (CreateOrderCommand.OrderItemData itemData : command.items()) {
            OrderItem newItem;
            if (itemData.type() == OrderItemType.MEDICATION) {
                newItem = createMedicationItem(itemData);
            } else if (itemData.type() == OrderItemType.PROCEDURE) {
                newItem = createProcedureItem(itemData);
            } else {
                newItem = createDiagnosticAidItem(itemData);
            }

            order.addItem(newItem);
        }


        Order savedOrder = orderRepository.save(order);


        eventPublisher.publishEvent(
                new OrderCreatedEvent(
                        savedOrder.getId(),
                        savedOrder.getOrderNumber(),
                        patientId.getValue()
                )
        );

        return savedOrder;
    }

    private void validateOrderItemCompatibility(List<CreateOrderCommand.OrderItemData> items) {
        boolean hasDiagnosticAid = items.stream().anyMatch(item -> item.type() == OrderItemType.DIAGNOSTIC_AID);
        boolean hasOtherTypes = items.stream().anyMatch(item ->
                item.type() == OrderItemType.MEDICATION || item.type() == OrderItemType.PROCEDURE);

        if (hasDiagnosticAid && hasOtherTypes) {
            throw new IllegalStateException(
                    "Una orden con ayuda diagnóstica no puede contener medicamentos o procedimientos. " +
                            "Las ayudas diagnósticas deben ser órdenes separadas."
            );
        }

        long distinctItemNumbers = items.stream()
                .map(CreateOrderCommand.OrderItemData::itemNumber)
                .distinct()
                .count();

        if (distinctItemNumbers != items.size()) {
            throw new IllegalStateException("Los números de ítem deben ser únicos dentro de la orden.");
        }

        boolean hasValidItemNumbers = items.stream()
                .map(CreateOrderCommand.OrderItemData::itemNumber)
                .sorted()
                .allMatch(itemNumber -> itemNumber >= 1 && itemNumber <= items.size());

        if (!hasValidItemNumbers) {
            throw new IllegalStateException(
                    "Los números de ítem deben ser consecutivos comenzando desde 1. " +
                            "Ejemplo: 1, 2, 3..."
            );
        }
    }

    private MedicationOrderItem createMedicationItem(CreateOrderCommand.OrderItemData data) {
        if (medicationRepository.findById(data.itemId()).isEmpty()) {
            throw new ResourceNotFoundException("Medicamento con ID " + data.itemId() + " no encontrado.");
        }
        return new MedicationOrderItem(data.itemNumber(), data.itemId(), data.dose(), data.treatmentDuration());
    }

    private ProcedureOrderItem createProcedureItem(CreateOrderCommand.OrderItemData data) {
        if (procedureRepository.findById(data.itemId()).isEmpty()) {
            throw new ResourceNotFoundException("Procedimiento con ID " + data.itemId() + " no encontrado.");
        }
        if (data.requiresSpecialist()) {
            if (specialistRepository.findById(data.specialistId()).isEmpty()) {
                throw new ResourceNotFoundException("Especialista con ID " + data.specialistId() + " no encontrado.");
            }
        }
        return new ProcedureOrderItem(
                data.itemNumber(),
                data.itemId(),
                data.quantity(),
                data.frequency(),
                data.requiresSpecialist(),
                data.specialistId()
        );
    }

    private DiagnosticAidOrderItem createDiagnosticAidItem(CreateOrderCommand.OrderItemData data) {
        if (diagnosticAidRepository.findById(data.itemId()).isEmpty()) {
            throw new ResourceNotFoundException("Ayuda diagnóstica con ID " + data.itemId() + " no encontrada.");
        }
        if (data.requiresSpecialist()) {
            if (specialistRepository.findById(data.specialistId()).isEmpty()) {
                throw new ResourceNotFoundException("Especialista con ID " + data.specialistId() + " no encontrado.");
            }
        }
        return new DiagnosticAidOrderItem(
                data.itemNumber(),
                data.itemId(),
                data.quantity(),
                data.requiresSpecialist(),
                data.specialistId()
        );
    }
}

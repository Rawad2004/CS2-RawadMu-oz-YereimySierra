package app.application.services;

import app.application.dto.CreateOrderCommand;
import app.application.usecases.DoctorUseCases.CreateOrderUseCase;
import app.application.usecases.OrderItemType;
import app.domain.model.Order;
import app.domain.model.Patient;
import app.domain.model.Staff;
import app.domain.model.order.*;
import app.domain.model.vo.NationalId;
import app.domain.model.vo.StaffRole;
import app.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import app.domain.exception.ResourceNotFoundException;

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


    public CreateOrderService(OrderRepositoryPort orderRepository, PatientRepositoryPort patientRepository,
                              StaffRepositoryPort staffRepository, MedicationRepositoryPort medicationRepository,
                              ProcedureRepositoryPort procedureRepository, DiagnosticAidRepositoryPort diagnosticAidRepository,
                              SpecialistRepositoryPort specialistRepository) {
        this.orderRepository = orderRepository;
        this.patientRepository = patientRepository;
        this.staffRepository = staffRepository;
        this.medicationRepository = medicationRepository;
        this.procedureRepository = procedureRepository;
        this.diagnosticAidRepository = diagnosticAidRepository;
        this.specialistRepository = specialistRepository;
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

        return orderRepository.save(order);
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
        return new ProcedureOrderItem(data.itemNumber(), data.itemId(), data.quantity(), data.frequency(), data.requiresSpecialist(), data.specialistId());
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
        return new DiagnosticAidOrderItem(data.itemNumber(), data.itemId(), data.quantity(), data.requiresSpecialist(), data.specialistId());
    }
}
package app.application.services;

import app.domain.model.*;
import app.domain.model.enums.InvoiceItemType;
import app.domain.model.order.*;
import app.domain.model.vo.InsurancePolicy;
import app.domain.model.vo.Money;
import app.domain.model.vo.NationalId;
import app.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderToInvoiceService {

    private final InvoiceService invoiceService;
    private final OrderRepositoryPort orderRepository;
    private final PatientRepositoryPort patientRepository;
    private final MedicationRepositoryPort medicationRepository;
    private final ProcedureRepositoryPort procedureRepository;
    private final DiagnosticAidRepositoryPort diagnosticAidRepository;

    public OrderToInvoiceService(InvoiceService invoiceService,
                                 OrderRepositoryPort orderRepository,
                                 PatientRepositoryPort patientRepository,
                                 MedicationRepositoryPort medicationRepository,
                                 ProcedureRepositoryPort procedureRepository,
                                 DiagnosticAidRepositoryPort diagnosticAidRepository) {
        this.invoiceService = invoiceService;
        this.orderRepository = orderRepository;
        this.patientRepository = patientRepository;
        this.medicationRepository = medicationRepository;
        this.procedureRepository = procedureRepository;
        this.diagnosticAidRepository = diagnosticAidRepository;
    }


    public Invoice convertOrderToInvoice(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + orderNumber));

        Patient patient = patientRepository.findByNationalId(order.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        List<app.application.port.in.GenerateInvoiceCommand.InvoiceItemCommand> invoiceItems =
                convertOrderItemsToInvoiceItems(order.getItems());

        String insuranceCompany = "";
        String policyNumber = "";
        Money policyDailyCoverage = Money.zero();
        LocalDate policyExpiryDate = LocalDate.now();
        boolean isPolicyActive = false;


        if (patient.getInsurancePolicy() != null) {
            InsurancePolicy insurancePolicy = patient.getInsurancePolicy();
            insuranceCompany = insurancePolicy.getCompanyName() != null ?
                    insurancePolicy.getCompanyName() : "";
            policyNumber = insurancePolicy.getPolicyNumber() != null ?
                    insurancePolicy.getPolicyNumber() : "";


            policyDailyCoverage = Money.zero();

            policyExpiryDate = insurancePolicy.getExpiryDate() != null ?
                    insurancePolicy.getExpiryDate() : LocalDate.now();

            isPolicyActive = insurancePolicy.isActive() &&
                    !policyExpiryDate.isBefore(LocalDate.now());
        }

        int patientAge = 0;
        if (patient.getBirthDate() != null && patient.getBirthDate().getValue() != null) {
            patientAge = calculateAge(patient.getBirthDate().getValue());
        }

        String doctorName = "Médico no especificado";
        if (order.getDoctorId() != null && order.getDoctorId().getValue() != null) {
            doctorName = "Dr. " + order.getDoctorId().getValue();
        }


        app.application.port.in.GenerateInvoiceCommand command =
                new app.application.port.in.GenerateInvoiceCommand(
                        patient.getNationalId() != null ? patient.getNationalId().getValue() : "",
                        patient.getFullName() != null ? patient.getFullName() : "Paciente sin nombre",
                        patientAge,
                        doctorName,
                        insuranceCompany,
                        policyNumber,
                        policyDailyCoverage,
                        policyExpiryDate,
                        isPolicyActive,
                        invoiceItems,
                        LocalDate.now().getYear()
                );

        return invoiceService.generateInvoice(command);
    }


    private List<app.application.port.in.GenerateInvoiceCommand.InvoiceItemCommand>
    convertOrderItemsToInvoiceItems(List<OrderItem> orderItems) {

        List<app.application.port.in.GenerateInvoiceCommand.InvoiceItemCommand> invoiceItems = new ArrayList<>();

        if (orderItems == null || orderItems.isEmpty()) {
            return invoiceItems;
        }

        for (OrderItem orderItem : orderItems) {
            try {
                if (orderItem instanceof MedicationOrderItem) {
                    invoiceItems.add(convertMedicationOrderItem((MedicationOrderItem) orderItem));
                } else if (orderItem instanceof ProcedureOrderItem) {
                    invoiceItems.add(convertProcedureOrderItem((ProcedureOrderItem) orderItem));
                } else if (orderItem instanceof DiagnosticAidOrderItem) {
                    invoiceItems.add(convertDiagnosticAidOrderItem((DiagnosticAidOrderItem) orderItem));
                }
            } catch (Exception e) {

                System.err.println("Error convirtiendo ítem de orden: " + e.getMessage());
            }
        }

        return invoiceItems;
    }

    private app.application.port.in.GenerateInvoiceCommand.InvoiceItemCommand
    convertMedicationOrderItem(MedicationOrderItem medicationItem) {


        if (medicationItem == null || medicationItem.getMedicationId() == null) {
            throw new IllegalArgumentException("MedicationOrderItem o su ID son nulos");
        }

        Medication medication = medicationRepository.findById(medicationItem.getMedicationId())
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado: " + medicationItem.getMedicationId()));

        String medicationName = medication.getName() != null ? medication.getName() : "Medicamento sin nombre";
        String dose = medicationItem.getDose() != null ? medicationItem.getDose() : "Dosis no especificada";
        String treatmentDuration = medicationItem.getTreatmentDuration() != null ?
                medicationItem.getTreatmentDuration() : "Duración no especificada";

        Money cost = medication.getCost() != null ? medication.getCost() : Money.zero();

        return new app.application.port.in.GenerateInvoiceCommand.InvoiceItemCommand(
                medicationName + " - " + dose,
                "MEDICATION",
                1,
                cost,
                "Dosis: " + dose + ", Duración: " + treatmentDuration
        );
    }

    private app.application.port.in.GenerateInvoiceCommand.InvoiceItemCommand
    convertProcedureOrderItem(ProcedureOrderItem procedureItem) {


        if (procedureItem == null || procedureItem.getProcedureId() == null) {
            throw new IllegalArgumentException("ProcedureOrderItem o su ID son nulos");
        }

        Procedure procedure = procedureRepository.findById(procedureItem.getProcedureId())
                .orElseThrow(() -> new IllegalArgumentException("Procedimiento no encontrado: " + procedureItem.getProcedureId()));

        String procedureName = procedure.getName() != null ? procedure.getName() : "Procedimiento sin nombre";
        String frequency = procedureItem.getFrequency() != null ? procedureItem.getFrequency() : "Frecuencia no especificada";
        Money cost = procedure.getCost() != null ? procedure.getCost() : Money.zero();

        String details = "Cantidad: " + procedureItem.getQuantity() + ", Frecuencia: " + frequency;
        if (procedureItem.isRequiresSpecialist()) {
            details += ", Requiere especialista";
        }

        return new app.application.port.in.GenerateInvoiceCommand.InvoiceItemCommand(
                procedureName,
                "PROCEDURE",
                procedureItem.getQuantity(),
                cost,
                details
        );
    }

    private app.application.port.in.GenerateInvoiceCommand.InvoiceItemCommand
    convertDiagnosticAidOrderItem(DiagnosticAidOrderItem diagnosticItem) {

        if (diagnosticItem == null || diagnosticItem.getDiagnosticAidId() == null) {
            throw new IllegalArgumentException("DiagnosticAidOrderItem o su ID son nulos");
        }

        DiagnosticAid diagnosticAid = diagnosticAidRepository.findById(diagnosticItem.getDiagnosticAidId())
                .orElseThrow(() -> new IllegalArgumentException("Ayuda diagnóstica no encontrada: " + diagnosticItem.getDiagnosticAidId()));

        String diagnosticName = diagnosticAid.getName() != null ? diagnosticAid.getName() : "Ayuda diagnóstica sin nombre";
        Money cost = diagnosticAid.getCost() != null ? diagnosticAid.getCost() : Money.zero();

        String details = "Cantidad: " + diagnosticItem.getQuantity();
        if (diagnosticItem.isRequiresSpecialist()) {
            details += ", Requiere especialista";
        }

        return new app.application.port.in.GenerateInvoiceCommand.InvoiceItemCommand(
                diagnosticName,
                "DIAGNOSTIC_AID",
                diagnosticItem.getQuantity(),
                cost,
                details
        );
    }


    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }


    public List<Invoice> generateInvoicesForPatient(String patientNationalId, int fiscalYear) {
        if (patientNationalId == null || patientNationalId.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del paciente no puede ser nula o vacía");
        }

        NationalId nationalId = new NationalId(patientNationalId);
        List<Order> patientOrders = orderRepository.findByPatientId(nationalId);

        List<Invoice> invoices = new ArrayList<>();
        for (Order order : patientOrders) {
            try {
                Invoice invoice = convertOrderToInvoice(order.getOrderNumber());
                invoices.add(invoice);
            } catch (Exception e) {
                System.err.println("Error generando factura para orden " +
                        (order.getOrderNumber() != null ? order.getOrderNumber() : "N/A") +
                        ": " + e.getMessage());
            }
        }

        return invoices;
    }
}

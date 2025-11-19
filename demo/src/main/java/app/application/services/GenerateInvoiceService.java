package app.application.services;

import app.application.usecases.BillingUseCases.GenerateInvoiceUseCase;
import app.domain.model.*;
import app.domain.model.vo.Money;
import app.domain.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@Transactional
public class GenerateInvoiceService implements GenerateInvoiceUseCase {

    private final PatientRepositoryPort patientRepository;
    private final OrderRepositoryPort orderRepository;
    private final InvoiceRepositoryPort invoiceRepository;
    private final CopaymentTrackerRepositoryPort copaymentTrackerRepository;

    public GenerateInvoiceService(PatientRepositoryPort patientRepository,
                                  OrderRepositoryPort orderRepository,
                                  InvoiceRepositoryPort invoiceRepository,
                                  CopaymentTrackerRepositoryPort copaymentTrackerRepository) {
        this.patientRepository = patientRepository;
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
        this.copaymentTrackerRepository = copaymentTrackerRepository;
    }

    @Override
    public Invoice generateInvoice(String patientNationalId,
                                   String orderNumber,
                                   LocalDate invoiceDate) {

        Patient patient = patientRepository.findByNationalId(new app.domain.model.vo.NationalId(patientNationalId))
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado."));

        int fiscalYear = invoiceDate.getYear();


        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Orden médica no encontrada."));

        List<InvoiceItem> items = order.convertToInvoiceItems();


        int age = Period.between(patient.getBirthDate().getValue(), LocalDate.now()).getYears();

        Invoice invoice = new Invoice(
                patientNationalId,
                patient.getFullName(),
                age,
                order.getDoctorName(),
                patient.getInsurancePolicy().getCompanyName(),
                patient.getInsurancePolicy().getPolicyNumber(),
                patient.getInsurancePolicy().getDailyCoverage(),
                patient.getInsurancePolicy().getExpiryDate(),
                patient.getInsurancePolicy().isActive(),
                items,
                fiscalYear
        );


        CopaymentTracker tracker =
                copaymentTrackerRepository.findOrCreateByPatientAndFiscalYear(
                        patientNationalId,
                        patient.getFullName(),
                        fiscalYear
                );

        if (tracker.isPatientExempt()) {
            invoice.applyCopaymentExemption();
        } else {
            tracker.addCopayment(invoice.getCopayment());
            copaymentTrackerRepository.save(tracker);

            if (tracker.isPatientExempt()) {
                invoice.applyCopaymentExemption();
            }
        }

        return invoiceRepository.save(invoice);
    }
}

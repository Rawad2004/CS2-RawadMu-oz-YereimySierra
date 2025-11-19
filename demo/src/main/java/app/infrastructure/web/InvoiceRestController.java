package app.infrastructure.web;

import app.application.usecases.BillingUseCases;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.CopaymentTracker;
import app.domain.model.Invoice;
import app.infrastructure.web.dto.CopaymentTrackerResponseDto;
import app.infrastructure.web.dto.InvoiceResponseDto;
import app.infrastructure.web.mapper.CopaymentTrackerWebMapper;
import app.infrastructure.web.mapper.InvoiceWebMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceRestController {

    private final BillingUseCases.GenerateInvoiceUseCase generateInvoiceUseCase;
    private final BillingUseCases.FindInvoiceUseCase findInvoiceUseCase;
    private final BillingUseCases.FindCopaymentTrackerUseCase findCopaymentTrackerUseCase;

    public InvoiceRestController(BillingUseCases.GenerateInvoiceUseCase generateInvoiceUseCase,
                                 BillingUseCases.FindInvoiceUseCase findInvoiceUseCase,
                                 BillingUseCases.FindCopaymentTrackerUseCase findCopaymentTrackerUseCase) {
        this.generateInvoiceUseCase = generateInvoiceUseCase;
        this.findInvoiceUseCase = findInvoiceUseCase;
        this.findCopaymentTrackerUseCase = findCopaymentTrackerUseCase;
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATIVE_STAFF','INFORMATION_SUPPORT')")
    @PostMapping("/from-order")
    public ResponseEntity<InvoiceResponseDto> generateFromOrder(
            @RequestBody @Valid GenerateInvoiceFromOrderRequest request
    ) {
        Invoice invoice = generateInvoiceUseCase.generateInvoice(
                request.getPatientNationalId(),
                request.getOrderNumber(),
                request.getInvoiceDate()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(InvoiceWebMapper.toDto(invoice));
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATIVE_STAFF','INFORMATION_SUPPORT','DOCTOR','NURSE')")
    @GetMapping("/{invoiceNumber}")
    public ResponseEntity<InvoiceResponseDto> getByInvoiceNumber(
            @PathVariable String invoiceNumber
    ) {
        Invoice invoice = findInvoiceUseCase.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Factura no encontrada con número: " + invoiceNumber
                ));

        return ResponseEntity.ok(InvoiceWebMapper.toDto(invoice));
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATIVE_STAFF','INFORMATION_SUPPORT','DOCTOR','NURSE')")
    @GetMapping("/by-patient/{nationalId}")
    public ResponseEntity<List<InvoiceResponseDto>> getByPatient(
            @PathVariable String nationalId,
            @RequestParam(name = "fiscalYear", required = false) Integer fiscalYear
    ) {
        List<Invoice> invoices;

        if (fiscalYear != null) {
            invoices = findInvoiceUseCase.findByPatientAndFiscalYear(nationalId, fiscalYear);
        } else {
            invoices = findInvoiceUseCase.findByPatientNationalId(nationalId);
        }

        return ResponseEntity.ok(InvoiceWebMapper.toDtoList(invoices));
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATIVE_STAFF','INFORMATION_SUPPORT')")
    @GetMapping
    public ResponseEntity<List<InvoiceResponseDto>> getAll() {
        List<Invoice> invoices = findInvoiceUseCase.findAll();
        return ResponseEntity.ok(InvoiceWebMapper.toDtoList(invoices));
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATIVE_STAFF','INFORMATION_SUPPORT')")
    @GetMapping("/copayments/{nationalId}")
    public ResponseEntity<CopaymentTrackerResponseDto> getCopaymentTracker(
            @PathVariable String nationalId,
            @RequestParam int fiscalYear
    ) {
        CopaymentTracker tracker = findCopaymentTrackerUseCase
                .findByPatientAndFiscalYear(nationalId, fiscalYear)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró tracker de copagos para el paciente " +
                                nationalId + " en el año fiscal " + fiscalYear
                ));

        return ResponseEntity.ok(CopaymentTrackerWebMapper.toDto(tracker));
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATIVE_STAFF','INFORMATION_SUPPORT')")
    @GetMapping("/copayments")
    public ResponseEntity<List<CopaymentTrackerResponseDto>> getCopaymentsByFiscalYear(
            @RequestParam int fiscalYear
    ) {
        List<CopaymentTracker> trackers = findCopaymentTrackerUseCase.findByFiscalYear(fiscalYear);
        return ResponseEntity.ok(CopaymentTrackerWebMapper.toDtoList(trackers));
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


    public static class GenerateInvoiceFromOrderRequest {

        @NotBlank(message = "La cédula del paciente es obligatoria")
        private String patientNationalId;

        @NotBlank(message = "El número de la orden es obligatorio")
        private String orderNumber;

        @NotNull(message = "La fecha de la factura es obligatoria")
        private LocalDate invoiceDate;

        public GenerateInvoiceFromOrderRequest() {
        }

        public GenerateInvoiceFromOrderRequest(String patientNationalId,
                                               String orderNumber,
                                               LocalDate invoiceDate) {
            this.patientNationalId = patientNationalId;
            this.orderNumber = orderNumber;
            this.invoiceDate = invoiceDate;
        }

        public String getPatientNationalId() {
            return patientNationalId;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public LocalDate getInvoiceDate() {
            return invoiceDate;
        }
    }
}

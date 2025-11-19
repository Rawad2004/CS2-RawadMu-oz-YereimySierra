package app.infrastructure.web.mapper;

import app.domain.model.Invoice;
import app.domain.model.InvoiceItem;
import app.domain.model.vo.Money;
import app.infrastructure.web.dto.InvoiceItemResponseDto;
import app.infrastructure.web.dto.InvoiceResponseDto;

import java.util.List;

public class InvoiceWebMapper {

    private InvoiceWebMapper() {}

    public static InvoiceResponseDto toDto(Invoice invoice) {
        if (invoice == null) return null;

        Money policyCoverage    = invoice.getPolicyDailyCoverage();
        Money subtotal          = invoice.getSubtotal();
        Money copayment         = invoice.getCopayment();
        Money insuranceCoverage = invoice.getInsuranceCoverage();
        Money total             = invoice.getTotal();

        List<InvoiceItemResponseDto> itemsDto = invoice.getItems().stream()
                .map(InvoiceWebMapper::toItemDto)
                .toList();

        return new InvoiceResponseDto(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getPatientNationalId(),
                invoice.getPatientName(),
                invoice.getPatientAge(),
                invoice.getDoctorName(),
                invoice.getInsuranceCompany(),
                invoice.getPolicyNumber(),
                policyCoverage != null ? policyCoverage.getAmount() : null,
                policyCoverage != null ? policyCoverage.getCurrency() : null,
                invoice.getPolicyEndDate(),
                itemsDto,
                subtotal != null ? subtotal.getAmount() : null,
                subtotal != null ? subtotal.getCurrency() : null,
                copayment != null ? copayment.getAmount() : null,
                copayment != null ? copayment.getCurrency() : null,
                insuranceCoverage != null ? insuranceCoverage.getAmount() : null,
                insuranceCoverage != null ? insuranceCoverage.getCurrency() : null,
                total != null ? total.getAmount() : null,
                total != null ? total.getCurrency() : null,
                invoice.getFiscalYear(),
                invoice.getCreatedAt(),
                invoice.getStatus() != null ? invoice.getStatus().name() : null
        );
    }

    public static InvoiceItemResponseDto toItemDto(InvoiceItem item) {
        if (item == null) return null;

        Money unitPrice = item.getUnitPrice();
        Money totalCost = item.getTotalCost();

        return new InvoiceItemResponseDto(
                item.getId(),
                item.getDescription(),
                item.getType() != null ? item.getType().name() : null,
                item.getQuantity(),
                unitPrice != null ? unitPrice.getAmount() : null,
                unitPrice != null ? unitPrice.getCurrency() : null,
                totalCost != null ? totalCost.getAmount() : null,
                totalCost != null ? totalCost.getCurrency() : null,
                item.getMedicationDosage(),
                item.getProcedureFrequency(),
                item.getDiagnosticDetails()
        );
    }

    public static List<InvoiceResponseDto> toDtoList(List<Invoice> invoices) {
        return invoices.stream()
                .map(InvoiceWebMapper::toDto)
                .toList();
    }
}

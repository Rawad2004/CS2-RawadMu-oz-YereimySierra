package app.infrastructure.web.mapper;

import app.domain.model.CopaymentTracker;
import app.domain.model.vo.Money;
import app.infrastructure.web.dto.CopaymentTrackerResponseDto;

import java.util.List;

public class CopaymentTrackerWebMapper {

    private CopaymentTrackerWebMapper() {}

    public static CopaymentTrackerResponseDto toDto(CopaymentTracker tracker) {
        if (tracker == null) return null;

        Money totalCopayment = tracker.getTotalCopayment();
        Money threshold      = tracker.getExemptionThreshold();

        return new CopaymentTrackerResponseDto(
                tracker.getId(),
                tracker.getPatientNationalId(),
                tracker.getPatientName(),
                tracker.getFiscalYear(),
                totalCopayment != null ? totalCopayment.getAmount() : null,
                totalCopayment != null ? totalCopayment.getCurrency() : null,
                threshold != null ? threshold.getAmount() : null,
                threshold != null ? threshold.getCurrency() : null,
                tracker.isExempt(),
                tracker.getLastUpdated()
        );
    }

    public static List<CopaymentTrackerResponseDto> toDtoList(List<CopaymentTracker> trackers) {
        return trackers.stream()
                .map(CopaymentTrackerWebMapper::toDto)
                .toList();
    }
}

package app.infrastructure.web.mapper;

import app.domain.model.DiagnosticAid;
import app.domain.model.vo.Money;
import app.infrastructure.web.dto.DiagnosticAidResponseDto;

import java.util.List;

public class DiagnosticAidWebMapper {

    private DiagnosticAidWebMapper() {
    }

    public static DiagnosticAidResponseDto toDto(DiagnosticAid diagnosticAid) {
        if (diagnosticAid == null) return null;

        Money cost = diagnosticAid.getCost();

        return new DiagnosticAidResponseDto(
                diagnosticAid.getId(),
                diagnosticAid.getName(),
                cost != null ? cost.getAmount() : null,
                cost != null ? cost.getCurrency() : null
        );
    }

    public static List<DiagnosticAidResponseDto> toDtoList(List<DiagnosticAid> diagnosticAids) {
        return diagnosticAids.stream()
                .map(DiagnosticAidWebMapper::toDto)
                .toList();
    }
}

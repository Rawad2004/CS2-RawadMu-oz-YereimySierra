package app.infrastructure.web.mapper;

import app.domain.model.Procedure;
import app.domain.model.vo.Money;
import app.infrastructure.web.dto.ProcedureResponseDto;

import java.util.List;

public class ProcedureWebMapper {

    private ProcedureWebMapper() {
    }

    public static ProcedureResponseDto toDto(Procedure procedure) {
        if (procedure == null) return null;

        Money cost = procedure.getCost();

        return new ProcedureResponseDto(
                procedure.getId(),
                procedure.getName(),
                cost != null ? cost.getAmount() : null,
                cost != null ? cost.getCurrency() : null
        );
    }

    public static List<ProcedureResponseDto> toDtoList(List<Procedure> procedures) {
        return procedures.stream()
                .map(ProcedureWebMapper::toDto)
                .toList();
    }
}

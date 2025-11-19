package app.infrastructure.web.mapper;

import app.domain.model.Medication;
import app.domain.model.vo.Money;
import app.infrastructure.web.dto.MedicationResponseDto;

import java.util.List;

public class MedicationWebMapper {

    private MedicationWebMapper() {}

    public static MedicationResponseDto toDto(Medication medication) {
        if (medication == null) return null;

        Money cost = medication.getCost();

        return new MedicationResponseDto(
                medication.getId(),
                medication.getName(),
                cost != null ? cost.getAmount() : null,
                cost != null ? cost.getCurrency() : null
        );
    }

    public static List<MedicationResponseDto> toDtoList(List<Medication> medications) {
        return medications.stream()
                .map(MedicationWebMapper::toDto)
                .toList();
    }
}

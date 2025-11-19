package app.application.port.in;

import java.time.LocalDate;

public record VisitDetailResponse(
        LocalDate visitDate,
        String doctorNationalId,
        String reasonForVisit,
        String symptomatology,
        String diagnosis,
        String orderNumber,
        String orderStatus,
        String orderType,
        LocalDate lastUpdateDate,
        String updateNotes,
        String bloodPressure,
        Double temperature,
        Integer pulse,
        Integer oxygenSaturation,
        String nurseNotes
) { }

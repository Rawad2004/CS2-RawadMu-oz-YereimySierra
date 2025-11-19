package app.application.services;

import app.application.port.in.VisitDetailResponse;
import app.application.usecases.DoctorUseCases;
import app.domain.exception.ResourceNotFoundException;
import app.domain.model.ClinicalHistoryEntry;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class GetVisitDetailService implements DoctorUseCases.GetVisitDetailUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public GetVisitDetailService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public VisitDetailResponse getVisitDetail(String patientNationalId, LocalDate visitDate) {
        if (patientNationalId == null || patientNationalId.isBlank()) {
            throw new IllegalArgumentException("La cédula del paciente es obligatoria");
        }
        if (visitDate == null) {
            throw new IllegalArgumentException("La fecha de la visita es obligatoria");
        }

        ClinicalHistoryEntry.VisitData visitData = clinicalHistoryRepository
                .findVisitByPatientAndDate(patientNationalId, visitDate)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró visita para el paciente " + patientNationalId +
                                " en la fecha " + visitDate
                ));

        return new VisitDetailResponse(
                visitDate,
                visitData.getDoctorNationalId(),
                visitData.getReasonForVisit(),
                visitData.getSymptomatology(),
                visitData.getDiagnosis(),
                visitData.getOrderNumber(),
                visitData.getOrderStatus() != null ? visitData.getOrderStatus().name() : null,
                visitData.getOrderType() != null ? visitData.getOrderType().name() : null,
                visitData.getLastUpdateDate(),
                visitData.getUpdateNotes(),
                visitData.getBloodPressure(),
                visitData.getTemperature(),
                visitData.getPulse(),
                visitData.getOxygenSaturation(),
                visitData.getNurseNotes()
        );
    }
}

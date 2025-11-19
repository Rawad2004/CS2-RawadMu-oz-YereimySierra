package app.application.services;

import app.application.port.in.VisitSummaryResponse;
import app.application.usecases.DoctorUseCases;
import app.domain.model.Visit;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetPatientVisitsService implements DoctorUseCases.GetPatientVisitsUseCase {

    private final ClinicalHistoryRepositoryPort clinicalHistoryRepository;

    public GetPatientVisitsService(ClinicalHistoryRepositoryPort clinicalHistoryRepository) {
        this.clinicalHistoryRepository = clinicalHistoryRepository;
    }

    @Override
    public List<VisitSummaryResponse> getVisits(String patientNationalId) {
        List<Visit> visits = clinicalHistoryRepository.findVisits(patientNationalId);

        return visits.stream()
                .map(v -> new VisitSummaryResponse(
                        v.getDate(),
                        v.getReason()
                ))
                .toList();
    }
}

package app.infrastructure.persistence;

import app.domain.model.ClinicalHistoryEntry;
import app.domain.model.Visit;
import app.domain.repository.ClinicalHistoryRepositoryPort;
import app.infrastructure.persistence.mongodb.ClinicalHistoryMongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ClinicalHistoryRepositoryAdapter implements ClinicalHistoryRepositoryPort {

    private final ClinicalHistoryMongoRepository mongoRepository;

    public ClinicalHistoryRepositoryAdapter(ClinicalHistoryMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public ClinicalHistoryEntry save(ClinicalHistoryEntry entry) {
        return mongoRepository.save(entry);
    }

    @Override
    public Optional<ClinicalHistoryEntry> findByPatientNationalId(String patientNationalId) {
        return mongoRepository.findByPatientNationalId(patientNationalId);
    }

    @Override
    public Optional<ClinicalHistoryEntry.VisitData> findVisitByPatientAndDate(String patientNationalId,
                                                                              LocalDate visitDate) {
        return mongoRepository.findByPatientNationalId(patientNationalId)
                .map(entry -> entry.getVisit(visitDate));
    }

    @Override
    public ClinicalHistoryEntry updateDiagnosis(String patientNationalId, LocalDate visitDate,
                                                String newDiagnosis, String updateNotes) {
        ClinicalHistoryEntry entry = mongoRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe historia clínica para el paciente " + patientNationalId));

        entry.updateDiagnosis(visitDate, newDiagnosis, updateNotes);
        return mongoRepository.save(entry);
    }

    @Override
    public ClinicalHistoryEntry addVisit(String patientNationalId, LocalDate visitDate,
                                         String doctorNationalId, String reasonForVisit,
                                         String symptomatology, String diagnosis,
                                         String orderNumber, ClinicalHistoryEntry.OrderType orderType) {

        ClinicalHistoryEntry entry = mongoRepository.findByPatientNationalId(patientNationalId)
                .orElseGet(() -> new ClinicalHistoryEntry(patientNationalId));

        entry.addVisit(visitDate, doctorNationalId, reasonForVisit, symptomatology,
                diagnosis, orderNumber, orderType);

        return mongoRepository.save(entry);
    }

    @Override
    public ClinicalHistoryEntry associateOrderToVisit(String patientNationalId, LocalDate visitDate,
                                                      String orderNumber, ClinicalHistoryEntry.OrderType orderType) {
        ClinicalHistoryEntry entry = mongoRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe historia clínica para el paciente " + patientNationalId));

        entry.associateOrder(visitDate, orderNumber, orderType);
        return mongoRepository.save(entry);
    }

    @Override
    public ClinicalHistoryEntry markVisitAsPendingDiagnostic(String patientNationalId, LocalDate visitDate) {
        ClinicalHistoryEntry entry = mongoRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe historia clínica para el paciente " + patientNationalId));

        entry.markAsPendingDiagnostic(visitDate);
        return mongoRepository.save(entry);
    }

    @Override
    public boolean existsVisit(String patientNationalId, LocalDate visitDate) {
        return mongoRepository.findByPatientNationalId(patientNationalId)
                .map(entry -> entry.hasVisitOnDate(visitDate))
                .orElse(false);
    }

    @Override
    public void deleteVisit(String patientNationalId, LocalDate visitDate) {
        ClinicalHistoryEntry entry = mongoRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe historia clínica para el paciente " + patientNationalId));

        if (!entry.hasVisitOnDate(visitDate)) {
            throw new IllegalArgumentException("No existe visita en la fecha " + visitDate);
        }

        entry.deleteVisit(visitDate);
        mongoRepository.save(entry);
    }

    @Override
    public List<Visit> findVisits(String patientNationalId) {
        return mongoRepository.findByPatientNationalId(patientNationalId)
                .map(entry -> {
                    Map<LocalDate, ClinicalHistoryEntry.VisitData> visitDataMap = entry.getVisitData();
                    if (visitDataMap == null || visitDataMap.isEmpty()) {
                        return Collections.<Visit>emptyList();
                    }

                    return visitDataMap.entrySet().stream()
                            .map(e -> {
                                LocalDate date = e.getKey();
                                ClinicalHistoryEntry.VisitData vd = e.getValue();
                                return new Visit(
                                        date,
                                        vd.getReasonForVisit(),
                                        vd.getSymptomatology()
                                );
                            })
                            .toList();
                })
                .orElse(Collections.emptyList());
    }

    @Override
    public void createVisit(String patientNationalId,
                            LocalDate visitDate,
                            String reason,
                            String notes) {

        ClinicalHistoryEntry entry = mongoRepository.findByPatientNationalId(patientNationalId)
                .orElseGet(() -> new ClinicalHistoryEntry(patientNationalId));

        entry.addVisit(
                visitDate,
                null,
                reason,
                notes,
                null
        );

        mongoRepository.save(entry);
    }

    @Override
    public ClinicalHistoryEntry registerVitalSigns(String patientNationalId,
                                                   LocalDate visitDate,
                                                   String bloodPressure,
                                                   Double temperature,
                                                   Integer pulse,
                                                   Integer oxygenSaturation,
                                                   String nurseNotes) {

        ClinicalHistoryEntry entry = mongoRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe historia clínica para el paciente " + patientNationalId));

        entry.registerVitalSigns(visitDate, bloodPressure, temperature, pulse, oxygenSaturation, nurseNotes);
        return mongoRepository.save(entry);
    }

    @Override
    public ClinicalHistoryEntry addNursingRecord(String patientNationalId,
                                                 LocalDate visitDate,
                                                 String orderNumber,
                                                 int itemNumber,
                                                 String description) {

        ClinicalHistoryEntry entry = mongoRepository.findByPatientNationalId(patientNationalId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe historia clínica para el paciente " + patientNationalId));

        if (!entry.hasVisitOnDate(visitDate)) {
            throw new IllegalArgumentException("No existe visita para la fecha: " + visitDate);
        }

        entry.addNursingRecord(visitDate, orderNumber, itemNumber, description);
        return mongoRepository.save(entry);
    }

}

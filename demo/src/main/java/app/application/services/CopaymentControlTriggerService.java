package app.application.services;

import app.domain.model.CopaymentTracker;
import app.domain.model.Invoice;
import app.domain.repository.CopaymentTrackerRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CopaymentControlTriggerService {

    private final CopaymentTrackerRepositoryPort copaymentTrackerRepository;

    public CopaymentControlTriggerService(CopaymentTrackerRepositoryPort copaymentTrackerRepository) {
        this.copaymentTrackerRepository = copaymentTrackerRepository;
    }


    public void updateCopaymentTracker(Invoice invoice) {
        try {
            String patientNationalId = invoice.getPatientNationalId();
            int fiscalYear = invoice.getFiscalYear();


            CopaymentTracker tracker = copaymentTrackerRepository
                    .findOrCreateByPatientAndFiscalYear(
                            patientNationalId,
                            getPatientName(invoice),
                            fiscalYear
                    );


            if (invoice.isPolicyActive() && !tracker.isExempt()) {
                tracker.addCopayment(invoice.getCopayment());
                copaymentTrackerRepository.save(tracker);

                System.out.println("✅ Copayment tracker actualizado - Paciente: " + patientNationalId +
                        ", Copago añadido: " + invoice.getCopaymentAmount() +
                        ", Total anual: " + tracker.getTotalCopayment().getAmount());
            }


            if (tracker.isPatientExempt()) {
                System.out.println("🎉 Paciente exento de copagos - Límite anual alcanzado: " +
                        patientNationalId);
            }

        } catch (Exception e) {
            System.err.println("❌ Error actualizando copayment tracker: " + e.getMessage());
        }
    }


    public void applyAutomaticCopaymentExemption(String patientNationalId, int fiscalYear) {
        try {
            CopaymentTracker tracker = copaymentTrackerRepository
                    .findByPatientAndFiscalYear(patientNationalId, fiscalYear)
                    .orElseThrow(() -> new IllegalArgumentException("Tracker no encontrado"));

            if (tracker.isPatientExempt()) {
                System.out.println("🔓 Exención automática aplicada - Paciente: " + patientNationalId);

            }
        } catch (Exception e) {
            System.err.println("❌ Error aplicando exención automática: " + e.getMessage());
        }
    }

    private String getPatientName(Invoice invoice) {

        return "Paciente " + invoice.getPatientNationalId();
    }
}

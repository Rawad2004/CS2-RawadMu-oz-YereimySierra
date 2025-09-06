// File: src/main/java/app/application/usecases/NurseUseCases.java
package app.application.usecases;

import app.application.dto.RecordMedicationAdministrationCommand;
import app.application.dto.RecordVitalSignsCommand;
import app.domain.model.Order;
import app.domain.model.Patient;
import java.util.List;
import java.util.Optional;

public interface NurseUseCases {

    interface FindPatientUseCase {
        Optional<Patient> findPatientByNationalId(String nationalId);
    }

    interface RecordVitalSignsUseCase {
        Patient recordVitalSigns(String patientNationalId, RecordVitalSignsCommand command);
    }

    interface RecordMedicationAdministrationUseCase {
        Patient recordMedicationAdministration(String patientNationalId, RecordMedicationAdministrationCommand command);
    }
    interface FindOrderUseCase {
        Optional<Order> findOrderByOrderNumber(String orderNumber);

        List<Order> findOrdersByPatientNationalId(String patientNationalId);

        List<Order> findOrdersByPatientNationalIdAndMedication(String patientNationalId, Long medicationId);
    }
}


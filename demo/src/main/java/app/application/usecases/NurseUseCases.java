// File: src/main/java/app/application/usecases/NurseUseCases.java
package app.application.usecases;

import app.application.port.in.RecordMedicationAdministrationCommand;
import app.application.port.in.RecordProcedureAdministrationCommand;
import app.application.port.in.RecordVitalSignsCommand;
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

    // NUEVO: Registrar procedimientos
    interface RecordProcedureAdministrationUseCase {
        Patient recordProcedureAdministration(String patientNationalId, RecordProcedureAdministrationCommand command);
    }

    interface FindOrderUseCase {
        Optional<Order> findOrderByOrderNumber(String orderNumber);
        List<Order> findOrdersByPatientNationalId(String patientNationalId);
        List<Order> findOrdersByPatientNationalIdAndMedication(String patientNationalId, Long medicationId);
    }

    // NUEVO: Buscar órdenes por procedimiento
    interface FindOrdersByProcedureUseCase {
        List<Order> findOrdersByPatientNationalIdAndProcedure(String patientNationalId, Long procedureId);
    }
}
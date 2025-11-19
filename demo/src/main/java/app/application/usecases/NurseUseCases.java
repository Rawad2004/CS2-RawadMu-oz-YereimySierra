package app.application.usecases;

import app.application.port.in.*;
import app.domain.model.Order;
import app.domain.model.Patient;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NurseUseCases {

    interface FindPatientUseCase {
        Optional<Patient> findPatientByNationalId(String nationalId);
    }

    interface RecordVitalSignsUseCase {
        Patient recordVitalSigns(String patientNationalId,
                                 RecordVitalSignsCommand command,
                                 LocalDate visitDate);
    }

    interface RecordMedicationAdministrationUseCase {
        Patient recordMedicationAdministration(String patientNationalId, RecordMedicationAdministrationCommand command);
    }

    interface RecordProcedureAdministrationUseCase {
        Patient recordProcedureAdministration(String patientNationalId, RecordProcedureAdministrationCommand command);
    }

    interface FindOrderUseCase {
        Optional<Order> findOrderByOrderNumber(String orderNumber);

        List<Order> findOrdersByPatientNationalId(String patientNationalId);

        List<Order> findOrdersByPatientNationalIdAndMedication(String patientNationalId, Long medicationId);
    }

    interface FindOrdersByProcedureUseCase {
        List<Order> findOrdersByPatientNationalIdAndProcedure(String patientNationalId, Long procedureId);
    }

    interface RecordNursingInterventionUseCase {
        void recordNursingIntervention(String patientNationalId,
                                       LocalDate visitDate,
                                       RecordNursingInterventionCommand command);
    }



    interface AddNursingRecordUseCase {
        void addNursingRecord(String patientNationalId,
                              LocalDate visitDate,
                              AddNursingRecordCommand command);
    }
}
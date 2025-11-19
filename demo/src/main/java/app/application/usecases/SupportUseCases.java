package app.application.usecases;

import app.application.port.in.*;
import app.domain.model.*;

import java.util.List;
import java.util.Optional;

public interface SupportUseCases {


    interface CreateMedicationUseCase {
        Medication createMedication(CreateMedicationCommand command);
    }

    interface CreateProcedureUseCase {
        Procedure createProcedure(CreateProcedureCommand command);
    }

    interface CreateDiagnosticAidUseCase {
        DiagnosticAid createDiagnosticAid(CreateDiagnosticAidCommand command);
    }

    interface CreateSpecialistUseCase {
        Specialist createSpecialist(CreateSpecialistCommand command);
    }

    interface FindMedicationUseCase {
        Optional<Medication> findById(Long id);
        Optional<Medication> findByName(String name);
        List<Medication> findAll();
        List<Medication> findAllActive();
    }

    interface FindProcedureUseCase {
        Optional<Procedure> findById(Long id);
        Optional<Procedure> findByName(String name);
        List<Procedure> findAll();
    }

    interface FindDiagnosticAidUseCase {
        Optional<DiagnosticAid> findById(Long id);
        Optional<DiagnosticAid> findByName(String name);
        List<DiagnosticAid> findAll();
    }

    interface FindSpecialistUseCase {
        Optional<Specialist> findById(Long id);
        Optional<Specialist> findBySpecialtyName(String specialtyName);
        List<Specialist> findAll();
    }

    // ===== UPDATE =====
    interface UpdateMedicationUseCase {
        Medication updateMedication(UpdateMedicationCommand command);
    }

    interface UpdateProcedureUseCase {
        Procedure updateProcedure(UpdateProcedureCommand command);
    }

    interface UpdateDiagnosticAidUseCase {
        DiagnosticAid updateDiagnosticAid(UpdateDiagnosticAidCommand command);
    }

    interface UpdateSpecialistUseCase {
        Specialist updateSpecialist(UpdateSpecialistCommand command);
    }

    // ===== DELETE =====
    interface DeleteMedicationUseCase {
        void deleteMedication(Long medicationId);
        void deleteMedicationByName(String name);
    }

    interface DeleteProcedureUseCase {
        void deleteProcedure(Long procedureId);
        void deleteProcedureByName(String name);
    }

    interface DeleteDiagnosticAidUseCase {
        void deleteDiagnosticAid(Long diagnosticAidId);
        void deleteDiagnosticAidByName(String name);
    }

    interface DeleteSpecialistUseCase {
        void deleteSpecialist(Long specialistId);
        void deleteSpecialistBySpecialtyName(String name);
    }
}

// File: src/main/java/app/application/usecases/SupportUseCases.java
package app.application.usecases;

import app.application.dto.CreateDiagnosticAidCommand;
import app.application.dto.CreateMedicationCommand;
import app.application.dto.CreateProcedureCommand;
import app.application.dto.CreateSpecialistCommand;
import app.domain.model.*;

public interface SupportUseCases {

    // Interface para crear medicamentos
    interface CreateMedicationUseCase {
        Medication createMedication(CreateMedicationCommand command);
    }

    // Interface para crear procedimientos
    interface CreateProcedureUseCase {
        Procedure createProcedure(CreateProcedureCommand command);
    }

    // Interface para crear ayudas diagnósticas
    interface CreateDiagnosticAidUseCase {
        DiagnosticAid createDiagnosticAid(CreateDiagnosticAidCommand command);
    }

    interface AuthenticateUseCase {
        Staff authenticate(String username, String password);
    }

    // Interface para crear especialistas
    interface CreateSpecialistUseCase {
        Specialist createSpecialist(CreateSpecialistCommand command);
    }
}

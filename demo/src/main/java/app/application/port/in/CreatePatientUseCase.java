// Ubicación: src/main/java/app/application/port/in/CreatePatientUseCase.java
package app.application.port.in;

import app.domain.model.Patient;

public interface CreatePatientUseCase {
    Patient createPatient(CreatePatientCommand command);
}
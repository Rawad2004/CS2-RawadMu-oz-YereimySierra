// File: src/main/java/app/application/port/in/UpdatePatientUseCase.java
package app.application.port.in;

import app.domain.model.Patient;

public interface UpdatePatientUseCase {
    Patient updatePatient(UpdatePatientCommand command);
}
// File: src/main/java/app/application/port/in/CreateSpecialistUseCase.java
package app.application.port.in;

import app.domain.model.Specialist;

public interface CreateSpecialistUseCase {
    Specialist createSpecialist(CreateSpecialistCommand command);
}

// File: src/main/java/app/application/port/in/CreateProcedureUseCase.java
package app.application.port.in;

import app.domain.model.Procedure;

public interface CreateProcedureUseCase {
    Procedure createProcedure(CreateProcedureCommand command);
}
// File: src/main/java/app/application/port/in/CreateDiagnosticAidUseCase.java
package app.application.port.in;

import app.domain.model.DiagnosticAid;

public interface CreateDiagnosticAidUseCase {
    DiagnosticAid createDiagnosticAid(CreateDiagnosticAidCommand command);
}
// File: src/main/java/app/application/dto/FindOrdersCommand.java
package app.application.port.in;

public record FindOrdersCommand(
        String patientNationalId,
        Long medicationId,
        String orderNumber
) { }
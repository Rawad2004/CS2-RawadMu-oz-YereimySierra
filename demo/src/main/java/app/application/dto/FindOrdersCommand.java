// File: src/main/java/app/application/dto/FindOrdersCommand.java
package app.application.dto;

public record FindOrdersCommand(
        String patientNationalId,
        Long medicationId,
        String orderNumber
) { }
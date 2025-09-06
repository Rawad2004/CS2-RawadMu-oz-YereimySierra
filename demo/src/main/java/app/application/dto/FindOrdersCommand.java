// File: src/main/java/app/application/dto/FindOrdersCommand.java
package app.application.dto;

public record FindOrdersCommand(
        String patientNationalId,
        Long medicationId,
        String orderNumber
) {
    public FindOrdersCommand {
        // Validar que al menos un criterio de búsqueda esté presente
        if (patientNationalId == null && medicationId == null && orderNumber == null) {
            throw new IllegalArgumentException("Debe proporcionar al menos un criterio de búsqueda");
        }
    }
}
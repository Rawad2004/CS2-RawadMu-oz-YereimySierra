package app.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateDiagnosticAidResultCommand(

        @NotBlank(message = "El diagnóstico es obligatorio")
        String diagnosis,

        @NotBlank(message = "Los resultados de la ayuda diagnóstica son obligatorios")
        String results
) {}

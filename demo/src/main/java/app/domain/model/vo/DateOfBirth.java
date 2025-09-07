package app.domain.model.vo;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class BirthDate implements Serializable {

    private LocalDate value;


    public BirthDate(LocalDate value) {

        System.out.println("-----------------------------------------");
        System.out.println("DEBUG: Entrando al constructor de BirthDate");
        System.out.println("DEBUG: Fecha recibida (value): " + value);
        System.out.println("DEBUG: Fecha actual (LocalDate.now()): " + LocalDate.now());
        System.out.println("DEBUG: Límite de 150 años (now - 150): " + LocalDate.now().minusYears(150));
        System.out.println("DEBUG: ¿La fecha recibida es anterior al límite? " + value.isBefore(LocalDate.now().minusYears(150)));
        System.out.println("-----------------------------------------");



        if (value == null) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula.");
        }
        if (value.isBefore(LocalDate.now().minusYears(150))) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser mayor a 150 años");
        }
        if (value.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser en el futuro");
        }
        this.value = value;
    }

    public LocalDate getValue() {
        return value;
    }
}
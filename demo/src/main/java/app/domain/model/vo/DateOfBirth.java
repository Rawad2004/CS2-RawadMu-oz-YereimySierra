package app.domain.model.vo;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class DateOfBirth implements Serializable {

    private LocalDate value;


    public DateOfBirth(LocalDate value) {

        if (value == null) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula.");
        }
        if (value.isBefore(LocalDate.now().minusYears(150))) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser hace más de 150 años");
        }
        if (value.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser en el futuro");
        }
        this.value = value;
    }

    public LocalDate getValue() {
        return value;
    }

    protected DateOfBirth(){}
}
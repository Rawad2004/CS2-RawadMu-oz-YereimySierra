package app.domain.model.vo;

import java.time.LocalDate;

public class BirthDate {

    private final LocalDate value;

    public BirthDate(LocalDate value) {

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

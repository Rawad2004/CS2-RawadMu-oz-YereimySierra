package app.domain.model.vo;

import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class Address implements Serializable {

    private String value;
    protected Address() {}
    private static final int MAX_LENGTH = 30;

    public Address(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede ser nula o vacía.");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("La dirección no puede exceder los " + MAX_LENGTH + " caracteres.");
        }

        this.value = value;
    }


    public String getValue() {
        return value;
    }
}

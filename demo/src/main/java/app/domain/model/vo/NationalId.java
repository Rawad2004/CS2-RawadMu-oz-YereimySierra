package app.domain.model.vo;

import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class NationalId implements Serializable {

    private String value;
    protected NationalId(){}

    public NationalId(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("El formato de numero de cedula no es válido.");
        }
        this.value = value;

    }


    public String getValue() {
        return value;
    }

    private boolean isValid(String nationalId) {
        if (nationalId == null || nationalId.trim().isEmpty() || nationalId.length() < 8) {
            return false;
        }

        return nationalId.matches("\\d{8,10}");
    }

}

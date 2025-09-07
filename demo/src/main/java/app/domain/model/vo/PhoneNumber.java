package app.domain.model.vo;


import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class PhoneNumber implements Serializable {

    private String value;
    protected PhoneNumber(){}

    public PhoneNumber(String value) {
        if (!isValid(value)){
            throw new IllegalArgumentException("El número de teléfono debe contener entre 1 y 10 dígitos numéricos.");
        }

        this.value = value;

    }

    public String getValue() {
        return value;
    }

    private boolean isValid(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }

        return phoneNumber.matches("\\d{1,10}");
    }
}

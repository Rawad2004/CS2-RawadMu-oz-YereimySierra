// File: src/main/java/app/domain/model/vo/Temperature.java
package app.domain.model.vo;

import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class Temperature implements Serializable {
    private Double value; // en Celsius
    protected Temperature(){}

    public Temperature(Double value) {
        if (value == null) {
            throw new IllegalArgumentException("La temperatura no puede ser nula");
        }
        if (value < 25.0 || value > 45.0) {
            throw new IllegalArgumentException("La temperatura debe estar entre 25°C y 45°C");
        }

        this.value = value;
    }

    public Double getValue() { return value; }

    @Override
    public String toString() {
        return value + "°C";
    }
}
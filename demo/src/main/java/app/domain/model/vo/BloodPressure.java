package app.domain.model.vo;

import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class BloodPressure implements Serializable {
    private Double value;

    protected BloodPressure() {}

    public BloodPressure(Double value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("La presión arterial debe ser positiva");
        }
        this.value = value;
    }

    public Double getValue() { return value; }

    @Override
    public String toString() {
        return value + " mmHg";
    }
}

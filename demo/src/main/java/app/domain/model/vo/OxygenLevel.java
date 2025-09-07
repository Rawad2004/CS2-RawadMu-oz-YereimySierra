// File: src/main/java/app/domain/model/vo/OxygenLevel.java
package app.domain.model.vo;


import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class OxygenLevel implements Serializable {
    private Double value;
    protected OxygenLevel(){}

    public OxygenLevel(Double value) {
        if (value == null) {
            throw new IllegalArgumentException("El nivel de oxígeno no puede ser nulo");
        }
        if (value < 0.0 || value > 100.0) {
            throw new IllegalArgumentException("El nivel de oxígeno debe estar entre 0% y 100%");
        }

        this.value = value;
    }

    public Double getValue() { return value; }

    @Override
    public String toString() {
        return value + "%";
    }
}
// File: src/main/java/app/domain/model/vo/Pulse.java
package app.domain.model.vo;

import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class Pulse implements Serializable {
    private Integer value; // latidos por minuto
    protected Pulse(){}

    public Pulse(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("El pulso no puede ser nulo");
        }
        if (value <= 0 || value > 250) {
            throw new IllegalArgumentException("El pulso debe estar entre 1 y 250 lpm");
        }

        this.value = value;
    }

    public Integer getValue() { return value; }

    @Override
    public String toString() {
        return value + " lpm";
    }
}

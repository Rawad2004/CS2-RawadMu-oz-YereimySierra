// File: src/main/java/app/domain/model/vo/Pulse.java
package app.domain.model.vo;

public class Pulse {
    private final Integer value; // latidos por minuto

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

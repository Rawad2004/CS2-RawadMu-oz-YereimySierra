// File: src/main/java/app/domain/model/vo/Temperature.java
package app.domain.model.vo;

public class Temperature {
    private final Double value; // en Celsius

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
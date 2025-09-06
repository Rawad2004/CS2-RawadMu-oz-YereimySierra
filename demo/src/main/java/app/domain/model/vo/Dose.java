// File: src/main/java/app/domain/model/vo/Dose.java
package app.domain.model.vo;

public class Dose {
    private final String value;

    public Dose(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("La dosis no puede ser nula o vacía");
        }
        if (value.length() > 50) {
            throw new IllegalArgumentException("La dosis no puede exceder 50 caracteres");
        }

        this.value = value;
    }

    public String getValue() { return value; }

    @Override
    public String toString() {
        return value;
    }
}

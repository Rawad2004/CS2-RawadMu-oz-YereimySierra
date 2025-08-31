package app.domain.model.vo;

public class Address {

    private final String value;
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
